package com.example.strongest.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

enum class AvatarSize {SMALL, MED, LARGE}
@Composable
fun Avatar(photoUrl: String, size: AvatarSize, modifier: Modifier = Modifier) {
    val avatarSize = when (size) {
        AvatarSize.SMALL -> 48
        AvatarSize.MED -> 64
        else -> 80
    }
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = if (size == AvatarSize.LARGE) 4 else 0

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .crossfade(true)
            .build(),
            contentDescription = "avatar",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
                .border(
                BorderStroke(borderWidth.dp, rainbowColorsBrush),
                CircleShape)
            )
        Badge(
            containerColor = Color.Green,
            modifier = Modifier.align(Alignment.BottomEnd).offset(0.dp, 0.dp).size(10.dp)
        )
    }
}