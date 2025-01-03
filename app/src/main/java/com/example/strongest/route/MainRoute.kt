package com.example.strongest.route

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strongest.component.dialog.LoadingDialog
import com.example.strongest.ui.screen.*
import com.example.strongest.viewmodel.AuthState
import com.example.strongest.viewmodel.AuthViewModel
import com.example.strongest.viewmodel.UserViewModel

@Composable
fun MainRoute(launchAuth: () -> Unit) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
    val authViewModel: AuthViewModel = viewModel()
    val isUserLogin = userViewModel.userState.collectAsState()

    when (authViewModel.authState) {
        is AuthState.NotLogin ->  {
            userViewModel.setAuthState(false)
            AppMainRoute(initScreen = Login.route, launchAuth = launchAuth, authViewModel = authViewModel)
        }
        is AuthState.DidLogin -> {
            if (!isUserLogin.value.isLogin) userViewModel.setAuthState(true)
            AppMainRoute(
                initScreen = Home.route,
                launchAuth = launchAuth,
                authViewModel = authViewModel
            )
        }
        is AuthState.LoginInProgress -> LoadingDialog(onDismissReq = {})
    }
}

@Composable
fun AppMainRoute(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    launchAuth: () -> Unit,
    authViewModel: AuthViewModel,
    initScreen: String) {
    fun onGoBack() {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = initScreen, modifier = modifier) {
        composable(Login.route) {
            LoginScreen(launchAuth = launchAuth, skipLogin = { navController.navigate(Home.route) })
        }
        composable(Home.route) {
            HomeScreen(navController, authViewModel, onProfileClick = {
                navController.navigate(Profile.route)
            })
        }
        composable(Profile.route) {
            ProfileScreen(onGoBack = { onGoBack() })
        }
        composable (route = Chat.routeWithArg, arguments = Chat.args) { navBackStackEntry ->
            val itemType = navBackStackEntry.arguments?.getString(Chat.chatIdArg)

            if (itemType != null) {
                ChatScreen(friendId = itemType, onGoBack = { onGoBack() })
            }
        }
    }
}