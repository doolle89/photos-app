package com.example.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey val id: String,
    val albumId: String,
    val title: String? = null,
    val description: String? = null,
    val mimetype: String? = null,
    val capturedDate: Long = 0L,
    val content: String? = null,
    val width: Int = 0,
    val height: Int = 0,
    val orientation: String? = null,
    val fileUrl: String? = null,
    val thumbnailUrl: String? = null,
    val videoUrl: String? = null,
    val livePhotoUrl: String? = null,
    val livePhotoType: String? = null,
    val hidden: Boolean = false,
    val excluded: Boolean = false,
)