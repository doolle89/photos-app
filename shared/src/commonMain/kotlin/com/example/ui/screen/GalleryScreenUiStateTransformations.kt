package com.example.ui.screen

import com.example.data.model.Album
import com.example.data.model.Photo


fun Album.toGalleryAlbumUiState(): GalleryAlbumUiState {
    return GalleryAlbumUiState(
        id = id,
        title = title,
        //createdDate = ,
        photos = photos.map { it.toGalleryPhotoUiState() }
    )
}

fun Photo.toGalleryPhotoUiState(): GalleryPhotoUiState {
    return GalleryPhotoUiState(
        id = id,
        title = title,
//capturedDate = Date(this.capturedDate),
        width = width,
        height = height,
        hidden = this.hidden,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        livePhotoUrl = this.livePhotoUrl,
        videoUrl = this.videoUrl
    )

}