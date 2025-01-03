package com.example.strongest.ui.screen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strongest.component.AppBarActions
import com.example.strongest.component.TopAppBar
import com.example.strongest.component.chat.ChatZone
import com.example.strongest.component.chat.MessageBox
import com.example.strongest.viewmodel.ChatViewModel

@Composable
fun ChatScreen(friendId: String, onGoBack: () -> Unit, modifier: Modifier = Modifier) {
    val chatViewModel: ChatViewModel = viewModel()
    val friendInfo = chatViewModel.friendInfo
    val messageUIState = chatViewModel.messagesFlow.reversed()
    Log.d("CHAT SCREEN", "message "+ messageUIState.size)

    val topBarActions = listOf(
        Icons.Default.Phone to {},
        Icons.Default.MoreVert to {}
    ).map { AppBarActions(it.first, it.second) }

    LaunchedEffect(Unit) {
        chatViewModel.getFriendInfo(friendId)
    }

    Scaffold(topBar = {
        TopAppBar(
            actionBtn = topBarActions,
            title = friendInfo?.name ?: "Unknown user",
            canGoBack = true,
            onGoBack = onGoBack
        )
    }) { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()) {
            val (chatView, typingZone) = createRefs()

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(color = Color.Magenta)
                    .constrainAs(chatView) {
                        bottom.linkTo(typingZone.top)
                    },
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = true,
            ) {
                messageUIState.forEachIndexed {index, items ->
                    item() {
                        Text(text = "Sections $index")
                    }
                    items(items, key = { it.sendAt }) {
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
                }
            }
            ChatZone(
                modifier = Modifier.constrainAs(typingZone) {
                    bottom.linkTo(parent.bottom)
                }, onSendMsg = chatViewModel::sendMessage
            )
        }
    }
}