package com.example.photos.gallery.model

import android.content.Context
import androidx.annotation.StringRes
import java.text.SimpleDateFormat
import java.util.*

data class GalleryAlbum(
    val id: String,
    val title: String? = null,
    val createdDate: Date,
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
    val capturedDate: Date,
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

    val timestampDay: String = dateFormat.format(capturedDate)

    companion object {
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    }
}

class StringResource(
    @StringRes private val id: Int,
    private vararg val formatArgs: Any = emptyArray()
) {
    fun resolve(context: Context): String {
        return context.getString(id, *formatArgs)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is StringResource) return false
        if (id != other.id) return false
        if (!formatArgs.contentEquals(other.formatArgs)) return false
        return true
    }

    override fun hashCode(): Int {
        return 31 * id + formatArgs.contentHashCode()
    }
}