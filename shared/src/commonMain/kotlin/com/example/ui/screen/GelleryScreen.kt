package com.example.ui.screen

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.data.RepositoryTemp
import com.example.data.remote.model.Album
import com.example.data.remote.model.Photo
import com.example.ui.AlbumUrls
import com.example.ui.view.AsyncImage

@Composable
fun GalleryScreen() {
    var album by remember { mutableStateOf<Album?>(null) }
    LaunchedEffect(Unit) {
        val repository = RepositoryTemp()
        album = repository.getAlbum(AlbumUrls.albumUrl)
    }
    album?.let { Album(it) }
}

@Composable
fun Album(
    album: Album
) {
    album.photos?.let { PhotoGrid(it.map { it.toGalleryPhoto() }) }
}

@Composable
fun PhotoGrid(
    photos: List<GalleryPhoto>
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