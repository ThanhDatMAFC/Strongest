package com.example.strongest.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeHeaderBar(modifier: Modifier = Modifier, scrollBehavior: TopAppBarScrollBehavior, onGoBack: () -> Unit) {
    LargeTopAppBar(
        title = { Text(text = "David Smith", overflow = TextOverflow.Ellipsis) },
        navigationIcon = { IconButton(onClick = onGoBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }},
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Avatar")
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}