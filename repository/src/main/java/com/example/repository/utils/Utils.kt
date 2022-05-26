package com.example.repository.utils

import com.example.repository.api.model.Album as AlbumApiModel
import com.example.repository.api.model.Photo as PhotoApiModel
import com.example.repository.database.model.Album as AlbumDatabaseModel
import com.example.repository.database.model.Photo as PhotoDatabaseModel

fun generateIdString(length: Int = 20) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun AlbumApiModel.toDatabaseModel(uri: String): AlbumDatabaseModel {
    return AlbumDatabaseModel(
        id = this.id ?: generateIdString(20),
        uri = uri,
        createdDate = this.createdDate,
        username = this.username,
        title = this.title,
        description = this.description,
        lastModified = this.lastModified,
    )
}

fun PhotoApiModel.toDatabaseModel(albumId: String): PhotoDatabaseModel {
    return PhotoDatabaseModel(
        id = this.id ?: generateIdString(20),
        albumId = albumId,
        title = this.title,
        description = this.description,
        mimetype = this.mimetype,
        capturedDate = this.capturedDate,
        content = this.content,
        width = this.width,
        height = this.height,
        orientation = this.orientation,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        videoUrl = this.videoUrl,
        livePhotoUrl = this.livePhotoUrl,
        livePhotoType = this.livePhotoType,
        hidden = this.hidden,
        excluded = this.excluded,
    )
}