package com.example.ui.screen.timeline

import cafe.adriel.voyager.core.model.screenModelScope
import com.example.data.Repository
import com.example.ui.AlbumUrls
import com.example.ui.util.screenmodel.UiStateScreenModel
import kotlinx.coroutines.launch

class TimelinePhotoCollectionScreenModel(
    private val repository: Repository = Repository.getInstance()
) : UiStateScreenModel<TimelinePhotoCollectionUiState>(TimelinePhotoCollectionUiState()) {
    private val albumId = AlbumUrls.albumUrl

    init {
        updateState(repository.observeAlbum(albumId)) { oldState, album ->
            album?.toTimelinePhotoCollectionUiState() ?: oldState
        }
        fetchAlbum()
    }

    private fun fetchAlbum() {
        screenModelScope.launch {
            repository.fetchAlbum(AlbumUrls.albumUrl)
        }
    }
}