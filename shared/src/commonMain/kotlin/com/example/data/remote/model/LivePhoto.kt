package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LivePhoto(
    val location: String? = null,
    val mimeType: String? = null,
    val deviceType: String? = null
)