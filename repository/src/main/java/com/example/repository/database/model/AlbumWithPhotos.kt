package com.example.repository.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithPhotos(
    @Embedded val album: Album,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val photos: List<Photo>
)