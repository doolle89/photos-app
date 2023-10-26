package com.example.ui.screen.timeline

import cafe.adriel.voyager.core.model.screenModelScope
import com.example.data.Repository
import com.example.ui.AlbumUrls
import com.example.ui.util.screenmodel.UiStateScreenModel
import kotlinx.coroutines.launch

class TimelineScreenModel(
    private val repository: Repository = Repository.getInstance()
) : UiStateScreenModel<TimelineUiState>(TimelineUiState()) {
    private val albumId = AlbumUrls.albumUrl

    init {
        updateState(repository.observeAlbum(albumId)) { oldState, album ->
            album?.toTimelineUiState() ?: oldState
        }
        fetchAlbum()
    }

    fun fetchAlbum() {
        screenModelScope.launch {
            repository.fetchAlbum(AlbumUrls.albumUrl)
        }
    }
}