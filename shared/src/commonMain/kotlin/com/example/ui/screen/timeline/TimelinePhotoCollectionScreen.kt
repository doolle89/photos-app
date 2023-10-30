package com.example.ui.screen.timeline

import androidx.compose.foundation.clickable
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


class TimelinePhotoCollectionScreen(
    private val onPhotoClick: (String) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TimelinePhotoCollectionScreenModel() }
        val timelinePhotoCollectionUiStateUiState by screenModel.uiState.collectAsState()

        TimelinePhotoCollection(
            timelinePhotoCollectionUiState = timelinePhotoCollectionUiStateUiState.state,
            onPhotoClick = onPhotoClick
        )
    }
}

@Composable
private fun TimelinePhotoCollection(
    timelinePhotoCollectionUiState: TimelinePhotoCollectionUiState,
    onPhotoClick: (String) -> Unit
) {
    TimelinePhotoGrid(
        timelineItems = timelinePhotoCollectionUiState.items,
        onPhotoClick = onPhotoClick
    )
}

@Composable
private fun TimelinePhotoGrid(
    timelineItems: List<TimelineItem>,
    gridCellsCount: Int = 3,
    onPhotoClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridCellsCount),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = timelineItems,
            key = { item ->
                when(item) {
                    is TimelineDateItem -> item.title
                    is TimelinePhotoItem -> item.id
                }
            },
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
                            modifier = Modifier
                                .aspectRatio(ratio = 1f)
                                .clickable { onPhotoClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}