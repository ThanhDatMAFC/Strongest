package com.example.strongest.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strongest.data.repo.AchievedMessageRepository
import com.example.strongest.model.AchievedMessage
import com.example.strongest.model.MessageModel
import com.example.strongest.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

const val DB_PATH = "https://strongest-442fa-default-rtdb.asia-southeast1.firebasedatabase.app/"

data class AchievedMsgState(val itemList: List<AchievedMessage> = listOf())

class ChatViewModel(private val achievedMsgRepo: AchievedMessageRepository) : ViewModel() {
    private val databaseRef = FirebaseDatabase.getInstance(DB_PATH).reference
    val userId: String? = Firebase.auth.currentUser?.uid

    private var chatId: String by mutableStateOf("")
    var friendInfo: UserModel? by mutableStateOf(null)
        private set
    var messagesFlow: SnapshotStateList<List<MessageModel>> = mutableStateListOf(mutableListOf())
    private val msgKeys = mutableListOf<String>()
    private var chatAvailability: Boolean = false
    val achievedMsgState: StateFlow<AchievedMsgState> = achievedMsgRepo.getAllMessagesStream()
       .map { AchievedMsgState(it) }
       .stateIn(
           scope = viewModelScope,
           started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
           initialValue = AchievedMsgState()
       )

    private suspend fun handleAchievedMessage() {
        //* Filter by chat Id and convert message to messageFlow
        val messageInChat = achievedMsgRepo.getAllMessagesStream().toList().first()
        messageInChat
            .filter { it.chatId == chatId }
            .forEach { divideMsgSection(it.message) }
        listenMsgChange()
    }

    private fun enterRoomChat(chatIds: List<String>) {
        val chatIdTemplate = userId + "_" + friendInfo?.id
        val room = chatIds.find(::findRoomChat)

        if (room != null) {
            chatId = room
            //* Get all chat history in local database
            databaseRef.child("chats").child(room).child("latestMsg").get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.getValue<MessageModel>()?.sender is String) {
                        viewModelScope.launch {
                            val messageInChat = achievedMsgRepo.getAllMessagesStream().toList().first()
                            messageInChat
                                .filter { it.chatId == chatId }
                                .forEach { divideMsgSection(it.message) }
                            listenMsgChange()
                        }
                    }
                }
        } else  {
            //* Create new room chat
            val key = databaseRef.child("chats").push().key
            databaseRef.updateChildren(mapOf("chats/$key" to chatIdTemplate))
            chatId = "$key/$chatIdTemplate"
        }
    }

    fun getFriendInfo(friendId: String) {
        databaseRef.child("users").orderByKey()
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friends = mutableListOf<UserModel>()
                    for (node in snapshot.children) {
                        node.getValue<UserModel>()?.let { friends.add(it) }
                    }
                    friendInfo = friends.find { friendId == it.id }
                    if (friendInfo !== null)
                        getChatIds()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getChatIds() {
        if (userId !is String) return

        val chatIds = mutableListOf<String>()
        //* Loop all nodes of userId node to find Room chat
        
        databaseRef.child("chats").orderByKey().get()
            .addOnSuccessListener {
                for (node in it.children) {
                    if (node.hasChildren()) {
                        loop@ for (childNode in node.children) {
                            chatIds.add("${node.key}/${childNode.key}")
                            break@loop
                        }
                    }
                }
                enterRoomChat(chatIds)
            }
    }

    private fun getChatDetail(msg: String) {
        val getDetailChat = databaseRef.child("chats").child(chatId).get()
        getDetailChat.addOnSuccessListener {
            val content = it.value as? HashMap<*, *>
            if (content != null) {
                ::chatAvailability.set(content.isNotEmpty())
                viewModelScope.launch {
                    sendMessage(msg)
                }
            }
            if (!chatAvailability) createNewChat(msg)
        }
    }

    private fun createNewChat(msg: String) {
        if (userId !is String) return

        val initChat: Map<String, Any> = mapOf(
            "isFriend" to false,
            "participants" to listOf(userId, friendInfo?.id)
        )
        databaseRef.child("chats").child(chatId).setValue(initChat)
            .addOnSuccessListener {
                ::chatAvailability.set(true)
                viewModelScope.launch {
                    sendMessage(msg)
                }
                listenMsgChange()
            }
    }

    suspend fun sendMessage(msg: String) {
        if (userId !is String) return

        if (!chatAvailability) {
            getChatDetail(msg)
        } else {
            //* Send message to an existed chat
            val newMessage =
                MessageModel(sender = userId, msg = msg, sendAt = DATE_FORMAT.format(Date()))
            val key =
                databaseRef.child("chats").child(chatId).child("messages").push().key
            val childUpdate = hashMapOf<String, Any>(
                "/chats/$chatId/messages/$key" to newMessage,
                "/chats/$chatId/latestMsg" to newMessage
            )

            Log.d(TAG, "Send message: $msg at ${newMessage.sendAt}")
            databaseRef.updateChildren(childUpdate)
            if (key != null) {
                saveAchievedMessage(key, newMessage)
            }
        }
    }

    private fun listenMsgChange() {
        if (userId == null) return
        databaseRef.child("chats").child(chatId).child("messages")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.key != null && snapshot.key !in msgKeys) {
                        msgKeys.add(snapshot.key.toString())
                        Log.d(TAG, "Listen new message: ${snapshot.key}")

                        val message = snapshot.getValue<HashMap<String,Any>>()
                        val sender = message?.get("sender").toString()
                        val msg = message?.get("msg").toString()
                        val readStatus = message?.get("readStatus") as Boolean
                        val sendAt = message?.get("sendAt").toString()
                        val msgModel = MessageModel(sender, msg, readStatus, sendAt)
                        divideMsgSection(msgModel)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(TAG, "ChildChanged: ${snapshot.value}")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) { }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

                override fun onCancelled(error: DatabaseError) { }

            })
    }

    private fun divideMsgSection(msg: MessageModel) {
        if (messagesFlow.isEmpty() || compareDateForNewSection(msg.sendAt, messagesFlow.last().lastOrNull()?.sendAt)) {
            // Break into a new section when same day and within 10 min
            val newSection = listOf(msg)
            messagesFlow.add(newSection)
        } else {
            // Add item to the last section
            val lastSection = messagesFlow.last().toMutableList()
            lastSection.add(msg)
            messagesFlow.removeAt(messagesFlow.count() - 1)
            messagesFlow.add(lastSection)
        }
    }

    private suspend fun saveAchievedMessage(msgKey: String, msg: MessageModel) {
        val achievedMessage = AchievedMessage(msgKey, chatId, msg)
        achievedMsgRepo.insertMessages(achievedMessage)
    }

    fun deleteServerMessage() {
        if (msgKeys.isNotEmpty())
            databaseRef.child("chats").child(chatId).child("messages").removeValue()
    }


    companion object {
        //TODO(REMOVE :ss of TIME_PATTERN)
        private const val TAG = "Chat ViewModel"
        private const val TIMEOUT_MILLIS = 5_000L
        const val DATE_PATTERN = "dd/MM/yyyy"
        const val TIME_PATTERN = "HH:mm:ss"
        private val DATE_FORMAT = SimpleDateFormat("$DATE_PATTERN $TIME_PATTERN")
    }
}

fun ChatViewModel.findRoomChat(chatId: String): Boolean {
    val targetId = chatId.substringAfter('/')
    val user1 = targetId.substringBefore('_')
    val user2 = targetId.substringAfter('_')
    return user1 == userId && user2 == friendInfo?.id || user1 == friendInfo?.id && user2 == userId
}

fun ChatViewModel.compareDateForNewSection(newDate: String?, lastDate: String?): Boolean {
    if (newDate == null || lastDate == null) {
        return false
    }

    val currentDate = LocalDate.parse(newDate.substringBefore(' '),
        DateTimeFormatter.ofPattern(ChatViewModel.DATE_PATTERN))
    val prevMessageDate = LocalDate.parse(lastDate.substringBefore(' '),
        DateTimeFormatter.ofPattern(ChatViewModel.DATE_PATTERN))

    if (currentDate.isEqual(prevMessageDate)) {
        val current = LocalTime.parse(
            newDate.substringAfter(' '),
            DateTimeFormatter.ofPattern(ChatViewModel.TIME_PATTERN)
        )
        val prevMessageTime = LocalTime.parse(
            lastDate.substringAfter(' '),
            DateTimeFormatter.ofPattern(ChatViewModel.TIME_PATTERN)
        )
        if (current.hour == prevMessageTime.hour)
            return current.minute - prevMessageTime.minute > 10
        return true
    } else if (currentDate.isAfter(prevMessageDate)) return true
    return false
}