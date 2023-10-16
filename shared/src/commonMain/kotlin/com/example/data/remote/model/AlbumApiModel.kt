package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumApiModel(
    val id: String? = null,
    val collectionId: Int = 0,
    val collectionType: Int = 0,
    val username: String? = null,
    val title: String? = null,
    val description: String? = null,
    val lastModified: Long = 0L,
    val total: Int = 0,
    val maxCapturedDate: Long = 0L,
    val minCapturedDate: Long = 0L,
    val photos: List<PhotoApiModel>? = null,
    val meta: String? = null,
    val shareInfo: ShareInfoApiModel? = null,
    val coverPhoto: PhotoApiModel? = null,
    val createdDate: Long = 0L
)