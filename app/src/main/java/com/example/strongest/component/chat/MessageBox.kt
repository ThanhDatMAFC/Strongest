package com.example.strongest.component.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strongest.ui.theme.Shapes

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    sender: String,
    msg: String,
    readStatus: Boolean = false,
    currentUser: String? = ""
) {
    val mine = sender == currentUser
    val boxColor =
        if (mine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val maxMsgBoxWidth = (screenWidth - 92)

    Row(modifier = modifier.padding(vertical = 4.dp)) {
        if (mine) {
            Spacer(modifier = Modifier.weight(1f))
            Card(
                colors = CardDefaults.cardColors(containerColor = boxColor),
                shape = Shapes.small,
            ) {
                Text(
                    text = msg,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(8.dp).requiredWidthIn(4.dp, maxMsgBoxWidth.dp)
                )

            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = boxColor),
                shape = Shapes.small
            ) {
                Text(
                    text = msg,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(8.dp).requiredWidthIn(4.dp, maxMsgBoxWidth.dp)
                )
            }
            Spacer(Modifier.fillMaxWidth().weight(1f))
        }
    }
}