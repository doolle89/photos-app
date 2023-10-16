package com.example.data.model

data class Photo(
    val id: String,
    val title: String?,
    val description: String?,
    val mimeType: String?,
    val capturedDate: Long = 0L,
    val content: String?,
    val width: Int = 0,
    val height: Int = 0,
    val orientation: String?,
    val fileUrl: String?,
    val thumbnailUrl: String?,
    val videoUrl: String?,
    val livePhotoUrl: String?,
    val livePhotoType: String?,
    val hidden: Boolean,
    val excluded: Boolean,
)