package com.example.ui.screen.timeline

import com.example.data.model.Album
import com.example.data.model.Photo
import com.example.ui.util.toLocalDateTime


fun Album.toTimelinePhotoCollectionUiState(): TimelinePhotoCollectionUiState {
    val datePhotoMap = mutableMapOf<String, MutableList<TimelinePhotoItem>>()
    photos.forEach { photo ->
        photo.toTimelinePhotoItemUiState().let { timelinePhotoItem ->
            datePhotoMap.getOrPut(timelinePhotoItem.timestampDay) { mutableListOf() }.add(timelinePhotoItem)
        }
    }
    val timelineItems = datePhotoMap.flatMap {
        listOf(TimelineDateItem(it.key), *it.value.toTypedArray())
    }
    return TimelinePhotoCollectionUiState(
        id = id,
        title = title,
        created = createdDate.toLocalDateTime(),
        items = timelineItems
    )
}

fun Photo.toTimelinePhotoItemUiState(): TimelinePhotoItem {
    return TimelinePhotoItem(
        id = id,
        title = title,
        captured = capturedDate.toLocalDateTime(),
        width = width,
        height = height,
        hidden = this.hidden,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        livePhotoUrl = this.livePhotoUrl,
        videoUrl = this.videoUrl
    )
}