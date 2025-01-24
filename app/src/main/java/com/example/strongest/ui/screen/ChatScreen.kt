package com.example.strongest.ui.screen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strongest.component.AppBarActions
import com.example.strongest.component.TopAppBar
import com.example.strongest.component.chat.ChatZone
import com.example.strongest.component.chat.MessageBox
import com.example.strongest.data.provider.DataViewModelProvider
import com.example.strongest.model.MessageModel
import com.example.strongest.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreen(friendId: String, onGoBack: () -> Unit, modifier: Modifier = Modifier) {
    val chatViewModel: ChatViewModel = viewModel(factory = DataViewModelProvider.Factory)
    val friendInfo = chatViewModel.friendInfo
    val messageUIState = chatViewModel.messagesFlow.reversed()
    val localMsg = chatViewModel.achievedMsgState.collectAsState()
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Log.d("CHAT SCREEN", "Local msg count:" + localMsg.value.itemList.count())

    val topBarActions = listOf(
        Icons.Default.Phone to {},
        Icons.Default.MoreVert to {}
    ).map { AppBarActions(it.first, it.second) }

    LaunchedEffect(Unit) {
        chatViewModel.getFriendInfo(friendId)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
        TopAppBar(
            actionBtn = topBarActions,
            title = friendInfo?.name ?: "Unknown user",
            canGoBack = true,
            onGoBack = {
                chatViewModel.deleteServerMessage()
                onGoBack()
            }
        )
    }) { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(padding)) {
            val (chatView, typingZone) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(chatView) {
                        bottom.linkTo(typingZone.top)
                    },
                verticalArrangement = Arrangement.Bottom,
                contentPadding = PaddingValues(8.dp),
                reverseLayout = true
            ) {
                messageUIState.forEachIndexed { _, items ->
                    items(items.reversed()) {
                        MessageBox(
                            sender = it.sender,
                            msg = it.msg,
                            readStatus = it.readStatus,
                            currentUser = chatViewModel.userId,
                            modifier = Modifier.animateItem(
                                fadeInSpec = tween(durationMillis = 250),
                                fadeOutSpec = tween(durationMillis = 100),
                                placementSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                        )
                    }
                    item {
                        if (items.isNotEmpty()) SectionHeader(items)
                    }
                }
            }
            ChatZone(
                modifier = Modifier.constrainAs(typingZone) {
                    bottom.linkTo(parent.bottom)
                }, onSendMsg = { msg -> coroutineScope.launch { chatViewModel.sendMessage(msg) }}
            )
        }
    }
}

@Composable
fun SectionHeader(items: List<MessageModel>) {
    val today = LocalDate.now()
    val sectionDate = LocalDate.parse(items[0].sendAt.substringBefore(' '), DateTimeFormatter.ofPattern(ChatViewModel.DATE_PATTERN))

    val sectionTitle = if (sectionDate.isEqual(today)) items[0].sendAt.substringAfter(' ') else items[0].sendAt

    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = sectionTitle, fontSize = 11.sp)
    }
}