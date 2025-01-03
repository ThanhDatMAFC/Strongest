package com.example.strongest.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.strongest.model.ChatItemModel
import com.example.strongest.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.runBlocking

sealed interface AuthState {
    data class DidLogin(val user: FirebaseUser?):AuthState
    object NotLogin: AuthState
    object LoginInProgress: AuthState
}

class AuthViewModel: ViewModel() {
    private val databaseRef = FirebaseDatabase.getInstance(DB_PATH).reference
    private var _auth: FirebaseAuth = Firebase.auth

    var authState: AuthState by mutableStateOf(AuthState.LoginInProgress)
        private set
    val chatFriends: SnapshotStateList<ChatItemModel> = mutableStateListOf()
    val friends: SnapshotStateList<UserModel> = mutableStateListOf()
    private val friendKeys = mutableListOf<String>()

    init {
        getUserInfo()
        Log.d(TAG, "Init auth view model")
    }

    private fun isUserExisted(): Boolean = friends.find { it.id == _auth.currentUser?.uid } != null

    private fun loginToDB(user: Map<String, String?>) {
        val key = databaseRef.child("users").push().key
        val childUpdate = hashMapOf<String, Any>(
            "/users/$key" to user,
        )
        databaseRef.updateChildren(childUpdate)
    }

    private fun saveUserInDb() {
        val user = _auth.currentUser
        if (user == null || isUserExisted()) {
            Log.d(TAG, "user is existed")
            return
        } else {
            Log.d(TAG, "user is not existed")
            loginToDB(
                mapOf(
                    "id" to user.uid,
                    "name" to user.displayName,
                    "email" to user.email,
                    "photo" to user.photoUrl.toString()
                )
            )
        }
    }

    private fun getFriendList() {
        databaseRef.child("users").orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.children.lastOrNull()?.key.toString() !in friendKeys) {
                        for (node in snapshot.children) {
                            node.getValue<UserModel>()?.let { friends.add(it) }
                            friendKeys.add(node.key.toString())
                        }
                        getFriendWithChat()
                        saveUserInDb()
                    }
                }

                override fun onCancelled(error: DatabaseError) { }
            })
    }

    private fun getFriendWithChat() {
        friends.forEach {
            val chatItemModel = ChatItemModel(
                friendId = it.id ?: "",
                avatarUrl = it.photo ?: "",
                chatName = it.name ?: "",
                message = it.email ?: "",
                time = "12:22",
                readStatus = false)
            chatFriends.add(chatItemModel)
        }
    }

    private fun getUserInfo() {
        val user = _auth.currentUser
        authState = if (user == null) {
            AuthState.NotLogin
        } else {
            getFriendList()
            AuthState.DidLogin(user)
        }
    }

    fun signOut() {
        _auth.signOut()
        authState = AuthState.NotLogin
    }

    companion object {
        val TAG = "AUTH_VIEW_MODEL"
    }
}