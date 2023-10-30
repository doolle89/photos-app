package com.example.ui.screen.timeline

import kotlinx.datetime.LocalDateTime

data class TimelinePhotoCollectionUiState(
    val id: String? = null,
    val title: String? = null,
    val created: LocalDateTime? = null,
    val items: List<TimelineItem> = emptyList()
)

sealed interface TimelineItem

data class TimelineDateItem(
    val title: String
) : TimelineItem

data class TimelinePhotoItem(
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
) : TimelineItem {
    val thumbnailUrlSmall get() = "${thumbnailUrl}.s"
    val thumbnailUrlMedium get() = "${thumbnailUrl}.m"
    val thumbnailUrlLarge get() = "${thumbnailUrl}.l"

    val timestampDay: String = captured.date.toString()
}