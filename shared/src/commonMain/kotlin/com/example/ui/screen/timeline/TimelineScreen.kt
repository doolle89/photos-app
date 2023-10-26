package com.example.ui.screen.timeline

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.ui.view.AsyncImage


object GalleryScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TimelineScreenModel() }
        val albumUiState by screenModel.uiState.collectAsState(null)
        TimelineAlbum(albumUiState?.state)
    }
}

@Composable
private fun TimelineAlbum(
    album: TimelineUiState?
) {
    album?.items?.let { TimelinePhotoGrid(it) }
}

@Composable
private fun TimelinePhotoGrid(
    timelineItems: List<TimelineItem>,
    gridCellsCount: Int = 3
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridCellsCount),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = timelineItems,
            span = { item ->
                when (item) {
                    is TimelineDateItem -> GridItemSpan(gridCellsCount)
                    is TimelinePhotoItem -> GridItemSpan(1)
                }
            }
        ) { item ->
            when (item) {
                is TimelineDateItem -> {
                    Text(
                        text = item.title
                    )
                }
                is TimelinePhotoItem -> {
                    if (item.thumbnailUrl != null) {
                        AsyncImage(
                            url = item.thumbnailUrlSmall,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.aspectRatio(ratio = 1f)
                        )
                    }
                }
            }
        }
    }
}