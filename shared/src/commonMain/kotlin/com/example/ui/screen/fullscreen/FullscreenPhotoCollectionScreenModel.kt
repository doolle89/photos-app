package com.example.ui.screen.fullscreen

import cafe.adriel.voyager.core.model.screenModelScope
import com.example.data.Repository
import com.example.ui.AlbumUrls
import com.example.ui.util.screenmodel.UiStateScreenModel
import com.example.ui.util.screenmodel.state
import kotlinx.coroutines.launch


class FullscreenPhotoCollectionScreenModel(
    private val repository: Repository = Repository.getInstance()
) : UiStateScreenModel<FullscreenPhotoCollectionUiState>(FullscreenPhotoCollectionUiState()) {
    private val albumId = AlbumUrls.albumUrl

    init {
        updateState(repository.observeAlbum(albumId)) { oldState, album ->
            album?.toFullscreenPhotoUiStateList() ?: oldState
        }
        fetchAlbum()
    }

    private fun fetchAlbum() {
        screenModelScope.launch {
            repository.fetchAlbum(AlbumUrls.albumUrl)
        }
    }

    fun calculatePhotoIndex(photoId: String): Int {
        return state.items.indexOfFirst { it.id == photoId }.coerceAtLeast(0)
    }
}