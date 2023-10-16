package com.example.ui.screen

data class GalleryAlbumUiState(
    val id: String,
    val title: String? = null,
   // val createdDate: Date,
    val photos: List<GalleryPhotoUiState>
)

sealed interface GalleryItemUiState {
    val id: String
    val positionId: Long get() = id.hashCode().toLong()
}

data class GalleryDayUiState(
    override val id: String,
    val title: String? = null,
    val photos: List<GalleryPhotoUiState>
) : GalleryItemUiState

data class GalleryPhotoUiState(
    override val id: String,
    val title: String? = null,
    //val capturedDate: Date,
    val width: Int,
    val height: Int,
    val hidden: Boolean,
    val fileUrl: String? = null,
    val thumbnailUrl: String? = null,
    val livePhotoUrl: String? = null,
    val videoUrl: String? = null
) : GalleryItemUiState {
    val thumbnailUrlSmall get() = "${thumbnailUrl}.s"
    val thumbnailUrlMedium get() = "${thumbnailUrl}.m"
    val thumbnailUrlLarge get() = "${thumbnailUrl}.l"

    //val timestampDay: String = dateFormat.format(capturedDate)

    companion object {
       // private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    }
}