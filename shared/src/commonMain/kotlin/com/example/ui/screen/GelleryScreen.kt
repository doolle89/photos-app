package com.example.ui.screen

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.data.Repository
import com.example.ui.AlbumUrls
import com.example.ui.view.AsyncImage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun GalleryScreen() {
    LaunchedEffect(Unit) {
        GalleryScreenViewModel.fetchAlbum()
    }

    val albumUiState by GalleryScreenViewModel.albumUiState.collectAsState(null)
    Album(albumUiState)
}

@Composable
fun Album(
    album: GalleryAlbumUiState?
) {
    album?.photos?.let { PhotoGrid(it) }
}

@Composable
fun PhotoGrid(
    photos: List<GalleryPhotoUiState>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos) { photo ->
            if (photo.thumbnailUrl != null) {
                AsyncImage(
                    url = photo.thumbnailUrlSmall,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.aspectRatio(ratio = 1f)
                )
            }
        }
    }
}