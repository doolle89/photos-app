package com.example.data.utils

import com.example.data.local.database.AlbumDbModel
import com.example.data.local.database.PhotoDbModel
import com.example.data.model.Album
import com.example.data.model.Photo
import com.example.data.remote.model.AlbumApiModel as AlbumApiModel
import com.example.data.remote.model.PhotoApiModel as PhotoApiModel

fun generateIdString(length: Int = 20) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun AlbumApiModel.toDatabaseModel(uri: String): AlbumDbModel {
    return AlbumDbModel(
        id = id ?: generateIdString(20),
        uri = uri,
        username = username,
        title = title,
        description = description,
        createdDate = createdDate,
        lastModified = lastModified,
        meta = meta,
        coverPhoto = coverPhoto?.id,
        total = total.toLong()
    )
}

fun PhotoApiModel.toDatabaseModel(): PhotoDbModel {
    return PhotoDbModel(
        id = this.id ?: generateIdString(20),
        title = this.title,
        description = this.description,
        mimeType = this.mimetype,
        capturedDate = this.capturedDate,
        content = this.content,
        width = this.width.toLong(),
        height = this.height.toLong(),
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

fun AlbumDbModel.toAlbum(photos: List<PhotoDbModel>): Album {
    val photoList = photos.map { it.toPhoto() }
    return Album(
        id = id,
        username = username,
        title = title,
        description = description,
        createdDate = createdDate ?: 0,
        lastModified = lastModified ?: 0,
        meta = meta,
        coverPhoto = photoList.find { it.id == coverPhoto },
        total = total ?: 0,
        photos = photoList
    )
}

fun PhotoDbModel.toPhoto(): Photo {
    return Photo(
        id = id,
        title = title,
        description = description,
        mimeType = mimeType,
        capturedDate = capturedDate ?: 0,
        content = content,
        width = width?.toInt() ?: 0,
        height = height?.toInt() ?: 0,
        orientation = orientation,
        fileUrl = fileUrl,
        thumbnailUrl = thumbnailUrl,
        videoUrl = videoUrl,
        livePhotoUrl = livePhotoUrl,
        livePhotoType = livePhotoType,
        hidden = hidden ?: false,
        excluded = excluded ?: false
    )
}