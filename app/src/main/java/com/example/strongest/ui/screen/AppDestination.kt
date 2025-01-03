package com.example.strongest.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val icon: ImageVector?
    val route: String
    val name: String
}

object Login: AppDestination {
    override val route = "login"
    override val name = "Login"
    override val icon = null
}

object Home: AppDestination {
    override val route = "home"
    override val name = "Home"
    override val icon = Icons.Default.Home
}

object  Profile: AppDestination
{
    override val route: String
        get() = "profile"
    override val name: String
        get() = "Me"
    override val icon: ImageVector
        get() = Icons.Default.AccountCircle
}
object Chat: AppDestination {
    override val icon = null
    override val name = "Chat"
    override val route = "chat"

    const val chatIdArg = "chat_id_arg"
    val routeWithArg = "$route/{$chatIdArg}"
    val args = listOf(navArgument(chatIdArg) { type = NavType.StringType})
}