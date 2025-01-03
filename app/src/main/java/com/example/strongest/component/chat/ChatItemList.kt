package com.example.strongest.component.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.strongest.model.ChatItemModel

@Composable
fun ChatItemList(chatItems: List<ChatItemModel>, modifier: Modifier = Modifier, onClickItem: (String) -> Unit) {
    LazyColumn (modifier = modifier.fillMaxWidth(), contentPadding = PaddingValues(8.dp)) {
        items(chatItems) {
            ChatItem(it.friendId, it.avatarUrl, it.chatName, it.message, it.time, it.readStatus, onClickItem)
        }
    }
}