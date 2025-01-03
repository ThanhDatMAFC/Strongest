package com.example.strongest.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawerSheet(closeDrawer: () -> Unit, modifier: Modifier = Modifier) {
    ModalDrawerSheet(modifier) {
        Column {
            TextButton(onClick = {}) {
                Text(text = "Login")
            }
            OutlinedButton(onClick = closeDrawer) {
                Text(text = "Close")
            }
        }
    }
}