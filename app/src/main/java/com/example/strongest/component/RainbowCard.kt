package com.example.strongest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strongest.ui.theme.Shapes

@Composable
fun RainbowCard(modifier: Modifier = Modifier, content: String) {
    val rainbowColorsBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color.DarkGray, Color.Red)
        )
    }

    Box(
        modifier = modifier
            .size(128.dp, 140.dp)
            .background(brush = rainbowColorsBrush, shape = Shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(text = content, color = Color.White, textAlign = TextAlign.Center, fontSize = 14.sp)
    }
}