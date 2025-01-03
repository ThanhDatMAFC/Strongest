package com.example.strongest.component.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strongest.component.Avatar
import com.example.strongest.component.AvatarSize

@Composable
fun ChatItem(friendId: String, avatarUrl: String, chatName: String, message: String, time: String, readStatus: Boolean, onClickItem: (String) -> Unit) {
    Row (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp).clickable { onClickItem(friendId) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically,) {
            Avatar(photoUrl = avatarUrl, size = AvatarSize.SMALL)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = chatName, fontWeight = FontWeight.SemiBold, lineHeight = 18.sp)
                Text(text = message, fontSize = 12.sp)
            }
        }
        Text(text = time, fontSize = 12.sp, fontWeight = FontWeight.Light, fontStyle = FontStyle.Italic)
    }
}