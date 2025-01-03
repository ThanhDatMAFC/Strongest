package com.example.strongest.component

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow

data class AppBarActions(val icon: ImageVector, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String = "", canSearch: Boolean =  false, canGoBack: Boolean = false, onGoBack: () -> Unit = {}, actionBtn: List<AppBarActions>) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Cyan, Color.Green, Color.Yellow, Color.LightGray)
        )
    }
    val searchFieldColor = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        cursorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedPlaceholderColor = Color.White,
        focusedPlaceholderColor = Color.White
    )

    CenterAlignedTopAppBar(
        title = {
            if (canSearch) TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "Search by phone No.") },
                leadingIcon = { AppBarButton(icon = Icons.Default.Search) },
                colors = searchFieldColor,
                textStyle = TextStyle(brush),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    showKeyboardOnFocus = true,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus()})
            )
            else Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            if (canGoBack) AppBarButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onGoBack
            )
        },
        actions = { actionBtn.map { AppBarButton(icon = it.icon, onClick = it.onClick) } }
    )
}

@Composable
fun AppBarButton(icon: ImageVector, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}