package com.example.strongest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.strongest.activity.launchAuthUiActivity
import com.example.strongest.route.MainRoute
import com.example.strongest.ui.theme.StrongestTheme

fun launchMainActivity(context: Context) {
    context.startActivity(Intent(context, MainActivity::class.java))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            StrongestTheme {
                MainRoute(launchAuth = {launchAuthUiActivity(this)})
            }
        }
    }
}
