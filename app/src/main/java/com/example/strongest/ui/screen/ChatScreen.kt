package com.example.strongest.ui.screen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strongest.component.AppBarActions
import com.example.strongest.component.TopAppBar
import com.example.strongest.component.chat.ChatZone
import com.example.strongest.component.chat.MessageBox
import com.example.strongest.model.MessageModel
import com.example.strongest.ui.theme.Shapes
import com.example.strongest.viewmodel.ChatViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun ChatScreen(friendId: String, onGoBack: () -> Unit, modifier: Modifier = Modifier) {
    val chatViewModel: ChatViewModel = viewModel()
    val friendInfo = chatViewModel.friendInfo
    val messageUIState = chatViewModel.messagesFlow.reversed()
    val state = rememberLazyListState()
    val hideKeyboard by remember {
        derivedStateOf{ state.firstVisibleItemIndex > 0}
    }
    Log.d("CHAT SCREEN", "$hideKeyboard "+ messageUIState.size)

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
            onGoBack = onGoBack
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
                }, onSendMsg = chatViewModel::sendMessage
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