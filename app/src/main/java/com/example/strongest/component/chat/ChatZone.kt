package com.example.strongest.component.chat

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.strongest.ui.theme.Shapes

@Composable
fun ChatZone(modifier: Modifier = Modifier, onSendMsg: (String) -> Unit) {
    var inputState by remember {
        mutableStateOf("")
    }
    val textFieldColor = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputState,
            onValueChange = { inputState = it },
            modifier = Modifier.weight(1f),
            shape = Shapes.medium,
            colors = textFieldColor,

        )
        IconButton(onClick = {
            onSendMsg(inputState)
            inputState = ""
            focusManager.clearFocus()
        }) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "send", tint = MaterialTheme.colorScheme.primary)
        }
    }
}