package com.example.strongest.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.example.strongest.component.ActivityIndicator

@Composable
fun LoadingDialog(onDismissReq: () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }
    Dialog(onDismissRequest = {
        openDialog.value = false
        onDismissReq()
    }) {
        Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            ActivityIndicator()
            Text(text = "Loading...")
        }
    }
}