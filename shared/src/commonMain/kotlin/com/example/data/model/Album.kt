package com.example.data.model

data class Album(
    val id: String,
    val username: String? = null,
    val title: String? = null,
    val description: String? = null,
    val createdDate: Long = 0L,
    val lastModified: Long = 0L,
    val meta: String? = null,
    val coverPhoto: Photo? = null,
    val total: Long = 0,
    val photos: List<Photo> = emptyList()
)