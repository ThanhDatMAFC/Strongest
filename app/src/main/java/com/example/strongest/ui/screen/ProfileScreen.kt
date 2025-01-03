package com.example.strongest.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.strongest.R
import com.example.strongest.component.LargeHeaderBar
import kotlinx.coroutines.launch

enum class ProfileScreen(
    val title: String,
    @DrawableRes val drawableRes: Int
) {
    GALLERY("My Gallery", R.drawable.baseline_dashboard),
    SAVE("Save", R.drawable.baseline_history)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onGoBack:() -> Unit) {
    val pages = ProfileScreen.values()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        topBar = {
            LargeHeaderBar(scrollBehavior = scrollBehavior, onGoBack = onGoBack)
        }
    ) { padding ->
        Column (modifier = Modifier.padding(padding)) {
            val coroutineScope = rememberCoroutineScope()
            TabRow(selectedTabIndex = pagerState.currentPage) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = page.title) },
                        icon = {
                            Icon(
                                painter = painterResource(id = page.drawableRes),
                                contentDescription = page.title
                            )
                        },
                        unselectedContentColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { index ->
                when (pages[index]) {
                    ProfileScreen.GALLERY -> {
                        GalleryTab()
                    }

                    ProfileScreen.SAVE -> {
                        SaveTab()
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryTab() {
     val sections = (0 until 30).toList().chunked(5)
    val itemModifier = Modifier.border(1.dp, Color.Blue).height(80.dp).wrapContentSize()

    LazyHorizontalGrid(rows = GridCells.Adaptive(128.dp)) {
        sections.forEachIndexed {index, items ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(text = "Section $index", modifier = itemModifier)
            }
            items(items) {
                Text(text = it.toString(), modifier = itemModifier)
            }
        }
    }
}

@Composable
fun SaveTab() {
    Card {
        Text(text = "Save")
    }
}