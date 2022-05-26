package com.example.repository.api.model

data class Album(
    val collectionId: Int = 0,
    val collectionType: Int = 0,
    val username: String? = null,
    val title: String? = null,
    val description: String? = null,
    val lastModified: Long = 0L,
    val total: Int = 0,
    val maxCapturedDate: Long = 0L,
    val minCapturedDate: Long = 0L,
    val photos: Array<Photo>? = null,
    val meta: String? = null,
    val shareInfo: ShareInfo? = null,
    val coverPhoto: Photo? = null,
    val createdDate: Long = 0L,
    val id: String? = null
)