package com.example.strongest.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.strongest.component.RainbowCard
import com.example.strongest.component.SmallHeaderBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onGoBack:() -> Unit) {
    val maxImageSize = 256.dp
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val rainbowColorsBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color.DarkGray, Color.Red)
        )
    }
    var currentImageSize by remember {
        mutableStateOf(maxImageSize)
    }
    var imageScale by remember {
        mutableFloatStateOf(1f)
    }
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newImageSize = currentImageSize + delta.dp
                val prevImageSize = currentImageSize

                currentImageSize = newImageSize.coerceIn(64.dp, maxImageSize)
                val consumed = currentImageSize - prevImageSize
                imageScale = currentImageSize / maxImageSize

                return Offset(0f, consumed.value)
            }
        }
    }

    Scaffold (
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            SmallHeaderBar(scrollBehavior = scrollBehavior, onGoBack = onGoBack)
        }
    ) { padding ->
        Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .offset {
                        IntOffset(0, (currentImageSize - 40.dp).roundToPx())
                    }
            ) {
                items(100, key = { it }) {
                    RainbowCard(content = "CARD $it", modifier = Modifier.animateItem())
                }
            }

            Box(modifier = Modifier
                .background(brush = rainbowColorsBrush)
                .height(80.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = if (imageScale < 0.3) 1f else 0f
                    translationY = -(maxImageSize.toPx()) / 2f

                }
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://developer.android.com/static/develop/ui/compose/images/graphics-sourceimagesmall.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(maxImageSize)
                    .graphicsLayer {
                        alpha = if (imageScale >= 0.3) imageScale else 0f
                        translationY = -(maxImageSize.toPx() - currentImageSize.toPx()) / 2f
                    }
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://developer.android.com/static/develop/ui/compose/images/graphics-sourceimagesmall.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 80.dp, height = 80.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .graphicsLayer {
                        alpha = if (imageScale < 0.3) 1f else 0f
                    }
            )
        }
    }
}

@Composable
@Preview()
fun ProfilePreview() {
    ProfileScreen({})
}