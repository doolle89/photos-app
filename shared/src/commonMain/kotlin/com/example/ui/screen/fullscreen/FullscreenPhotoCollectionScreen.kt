@file:OptIn(ExperimentalFoundationApi::class)

package com.example.ui.screen.fullscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.ui.view.AsyncImage

class FullscreenPhotoCollectionScreen(
    private val photoId: String
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { FullscreenPhotoCollectionScreenModel() }
        val uiState by screenModel.uiState.collectAsState()
        val initialIndex = screenModel.calculatePhotoIndex(photoId)

        FullscreenPhotoCollection(
            items = uiState.state.items,
            initialIndex = initialIndex
        )
    }
}

@Composable
fun FullscreenPhotoCollection(
    items: List<FullscreenPhotoUiState>,
    initialIndex: Int
) {
    val pagerState = rememberPagerState(
        pageCount = { items.size }
    )
    LaunchedEffect(items) {
        pagerState.scrollToPage(initialIndex)
    }
    HorizontalPager(
        state = pagerState,
    ) { page ->
        val item = items[page]
        AsyncImage(
            url = item.thumbnailUrlLarge,
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                AsyncImage(
                    url = item.thumbnailUrlSmall,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        )
    }
}