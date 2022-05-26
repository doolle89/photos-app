package com.example.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey val id: String,
    val uri: String,
    val createdDate: Long = 0L,
    val username: String? = null,
    val title: String? = null,
    val description: String? = null,
    val lastModified: Long = 0L,
)