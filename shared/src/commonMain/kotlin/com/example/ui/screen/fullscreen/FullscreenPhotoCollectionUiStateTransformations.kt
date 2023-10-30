package com.example.ui.screen.fullscreen

import com.example.data.model.Album
import com.example.data.model.Photo
import com.example.ui.util.toLocalDateTime

fun Album.toFullscreenPhotoUiStateList(): FullscreenPhotoCollectionUiState {
    return FullscreenPhotoCollectionUiState(
        id = id,
        title = title,
        created = createdDate.toLocalDateTime(),
        items = photos.map { it.toFullscreenPhotoUiState() }
    )
}

fun Photo.toFullscreenPhotoUiState(): FullscreenPhotoUiState {
    return FullscreenPhotoUiState(
        id = id,
        title = title,
        captured = capturedDate.toLocalDateTime(),
        width = width,
        height = height,
        hidden = this.hidden,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        livePhotoUrl = this.livePhotoUrl,
        videoUrl = this.videoUrl
    )
}