package com.example.strongest.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallHeaderBar(modifier: Modifier = Modifier, scrollBehavior: TopAppBarScrollBehavior, onGoBack: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = { IconButton(onClick = onGoBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }},
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Avatar")
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = Color.White
        )
    )
}