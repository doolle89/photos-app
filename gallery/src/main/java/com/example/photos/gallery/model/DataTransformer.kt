package com.example.photos.gallery.model

import com.example.repository.database.model.AlbumWithPhotos
import com.example.repository.database.model.Photo
import java.util.*


fun AlbumWithPhotos.toGalleryAlbum(): GalleryAlbum {
    return GalleryAlbum(
        id = this.album.id,
        createdDate = Date(album.createdDate),
        photos = this.photos.map { it.toGalleryPhoto() }
    )
}

fun AlbumWithPhotos.toGalleryDays(): List<GalleryDay> {
    val daysMap = mutableMapOf<String, MutableList<GalleryPhoto>>()
    this.photos.forEach { photo ->
        photo.toGalleryPhoto().let { galleryPhoto ->
            daysMap.getOrPut(galleryPhoto.timestampDay) { mutableListOf() }.add(galleryPhoto)
        }
    }
    return daysMap.map { (day, photos) ->
        GalleryDay(
            id = day,
            title = day,
            photos = photos
        )
    }
}

fun Photo.toGalleryPhoto(): GalleryPhoto {
    return GalleryPhoto(
        id = this.id,
        title = this.title,
        capturedDate = Date(this.capturedDate),
        width = this.width,
        height = this.height,
        hidden = this.hidden,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        livePhotoUrl = this.livePhotoUrl,
        videoUrl = this.videoUrl
    )
}