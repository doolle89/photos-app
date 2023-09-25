package com.example.ui.screen

import com.example.data.remote.model.Photo

data class GalleryAlbum(
    val id: String,
    val title: String? = null,
   // val createdDate: Date,
    val photos: List<GalleryPhoto>
)

sealed interface GalleryItem {
    val id: String
    val positionId: Long get() = id.hashCode().toLong()
}

data class GalleryDay(
    override val id: String,
    val title: String? = null,
    val photos: List<GalleryPhoto>
) : GalleryItem

data class GalleryPhoto(
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
) : GalleryItem {
    val thumbnailUrlSmall get() = "${thumbnailUrl}.s"
    val thumbnailUrlMedium get() = "${thumbnailUrl}.m"
    val thumbnailUrlLarge get() = "${thumbnailUrl}.l"

    //val timestampDay: String = dateFormat.format(capturedDate)

    companion object {
       // private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    }
}



fun Photo.toGalleryPhoto(): GalleryPhoto {
    return GalleryPhoto(
        id = this.id ?: "",
        title = this.title,
        //capturedDate = Date(this.capturedDate),
        width = this.width,
        height = this.height,
        hidden = this.hidden,
        fileUrl = this.fileUrl,
        thumbnailUrl = this.thumbnailUrl,
        livePhotoUrl = this.livePhotoUrl,
        videoUrl = this.videoUrl
    )
}