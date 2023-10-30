package com.example.ui.screen.fullscreen

import kotlinx.datetime.LocalDateTime


data class FullscreenPhotoCollectionUiState(
    val id: String? = null,
    val title: String? = null,
    val created: LocalDateTime? = null,
    val items: List<FullscreenPhotoUiState> = emptyList()
)

data class FullscreenPhotoUiState(
    val id: String,
    val title: String? = null,
    val captured: LocalDateTime,
    val width: Int,
    val height: Int,
    val hidden: Boolean,
    val fileUrl: String? = null,
    val thumbnailUrl: String? = null,
    val livePhotoUrl: String? = null,
    val videoUrl: String? = null
) {
    val thumbnailUrlSmall get() = "${thumbnailUrl}.s"
    val thumbnailUrlMedium get() = "${thumbnailUrl}.m"
    val thumbnailUrlLarge get() = "${thumbnailUrl}.l"
}