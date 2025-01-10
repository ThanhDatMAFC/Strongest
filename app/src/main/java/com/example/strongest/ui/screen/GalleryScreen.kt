package com.example.strongest.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.strongest.R
import kotlinx.coroutines.launch

enum class GalleryScreen(
    val title: String,
    @DrawableRes val drawableRes: Int
) {
    GALLERY("My Gallery", R.drawable.baseline_dashboard),
    SAVE("Save", R.drawable.baseline_history)
}

@Composable
fun GalleryScreen() {
    val pages = GalleryScreen.values()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column {
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

        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top, ) { index ->
            when (pages[index]) {
                GalleryScreen.GALLERY -> {
                    GalleryTab()
                }

                GalleryScreen.SAVE -> {
                    SaveTab()
                }
            }
        }
    }
}

@Composable
fun GalleryTab() {
    val sections = (0 until 30).toList().chunked(5)
    val itemModifier = Modifier
        .border(1.dp, Color.Blue)
        .height(80.dp)
        .wrapContentSize()

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