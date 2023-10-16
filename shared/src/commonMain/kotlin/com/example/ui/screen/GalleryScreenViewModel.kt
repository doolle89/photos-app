package com.example.ui.screen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.data.Repository
import com.example.ui.AlbumUrls
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object GalleryScreenViewModel {
    private val repository = Repository.getInstance()
    private val albumId = AlbumUrls.albumUrl

    val albumUiState: Flow<GalleryAlbumUiState?> = repository.observeAlbum(albumId).map { it?.toGalleryAlbumUiState() }

    fun fetchAlbum() {
        viewModelScope.launch {
            repository.fetchAlbum(AlbumUrls.albumUrl)
        }
    }


}

val GalleryScreenViewModel.viewModelScope get() = GlobalScope