package com.example.strongest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.strongest.component.ActivityIndicator

/*
    SIGNING CONFIG:
    MD5: CA:C5:F0:A9:96:DC:B8:51:AB:45:00:E0:72:5F:77:0A
    SHA1: D4:EF:16:1F:61:87:10:8E:2F:0C:F7:D6:AA:B3:2D:87:EB:AA:4A:C6
    SHA-256: 7A:D1:E6:BB:B8:1B:A8:38:6D:BB:E5:E0:05:E3:A1:B9:94:E1:A0:8D:58:74:73:CD:75:61:AA:B9:79:39:8D:30
 */
@Composable
fun LoginScreen(launchAuth: () -> Unit, launchFBLogin: () -> Unit, skipLogin: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold (floatingActionButton = {
        TextButton(onClick = skipLogin) {
            Text(text = "Skip")
        }
    }) { padding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()) {
            Text(text = "Strongest")
            Button(onClick = launchAuth) {
                Text(text = "Login with Google")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = launchFBLogin) {
                Text(text = "Login with Facebook")
            }

        }
    }
}