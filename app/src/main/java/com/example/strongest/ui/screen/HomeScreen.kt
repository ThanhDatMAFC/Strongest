package com.example.strongest.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.strongest.component.AppBarActions
import com.example.strongest.component.DrawerSheet
import com.example.strongest.component.TopAppBar
import com.example.strongest.component.chat.ChatItemList
import com.example.strongest.model.ChatItemModel
import com.example.strongest.viewmodel.AuthState
import com.example.strongest.viewmodel.AuthViewModel
import com.example.strongest.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    onProfileClick: (id: String) -> Unit,
    modifier: Modifier = Modifier) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
    val friends = authViewModel.chatFriends
    val didUserLogin = userViewModel.userState.collectAsState()
    val isLogin by lazy { didUserLogin.value.isLogin } // lazy: the value is computed only on first access

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()
    val isAtTopList by remember {
        derivedStateOf { listState.firstVisibleItemIndex < 2 }
    }

    fun openChatRoom(friendId: String) {
        navHostController.navigate("${Chat.route}/$friendId")
    }

    fun openDrawer() {
        coroutineScope.launch { drawerState.open() }
    }

    val topBarActions = listOf(
        Icons.Default.AccountCircle to {onProfileClick("go to my_profile")},
        Icons.Default.Menu to { openDrawer() }
    ).map { AppBarActions(it.first, it.second) }

    Log.d("HOME_SCREEN", friends.size.toString())
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(closeDrawer = { coroutineScope.launch { drawerState.close() } })
        }
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = { TopAppBar(canSearch = true, actionBtn = topBarActions) }
        ) { padding ->
            ConstraintLayout(modifier = modifier
                .padding(padding)
                .fillMaxSize()) {
                val (chatList, buttons, scrollCtrBtn) = createRefs()

                Row(modifier = Modifier.constrainAs(buttons) {
                    top.linkTo(parent.top, margin = 8.dp)
                }) {
                    Button(onClick = { }) {
                        Text(text = "New chat")
                    }
                    Button(onClick = authViewModel::signOut) {
                        Text(text = "Log out")
                    }
                }
                ChatItemList(
                    chatItems = friends,
                    onClickItem = { openChatRoom(it) },
                    modifier = Modifier.constrainAs(chatList) {
                        top.linkTo(buttons.bottom)
                    }
                )
                AnimatedVisibility(
                    visible = isAtTopList,
                    modifier = Modifier.constrainAs(scrollCtrBtn) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Top")
                    }
                }
            }
        }
    }
}
