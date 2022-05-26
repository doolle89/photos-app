package com.example.photos.gallery.gallerylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.photos.gallery.databinding.ItemGalleryDayBinding
import com.example.photos.gallery.databinding.ItemGalleryPhotoBinding
import com.example.photos.gallery.gallerylist.viewholder.GalleryDayViewHolder
import com.example.photos.gallery.gallerylist.viewholder.GalleryPhotoViewHolder
import com.example.photos.gallery.model.GalleryDay
import com.example.photos.gallery.model.GalleryItem
import com.example.photos.gallery.model.GalleryPhoto
import com.example.photos.gallery.utils.GalleryItemDiffUtilCallback

class GalleryListAdapter(
    private val onPhotoLoadedListener: ((GalleryItem, Boolean) -> Unit)? = null,
    private val onItemClickListener: ((View, GalleryItem) -> Unit)? = null
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class ViewType {
        Day,
        Photo
    }

    private val listDiffer = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(GalleryItemDiffUtilCallback).build()
    )

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Day -> {
                val binding = ItemGalleryDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                GalleryDayViewHolder(binding)
            }
            ViewType.Photo -> {
                val binding = ItemGalleryPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GalleryPhotoViewHolder(binding, onPhotoLoadedListener, onItemClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GalleryDay -> {
                (holder as GalleryDayViewHolder).bind(item)
            }
            is GalleryPhoto -> {
                (holder as GalleryPhotoViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GalleryDay -> ViewType.Day.ordinal
            is GalleryPhoto -> ViewType.Photo.ordinal
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).positionId
    }

    private fun getItem(position: Int): GalleryItem {
        return listDiffer.currentList[position]
    }

    fun submitList(list: List<GalleryItem>?) {
        listDiffer.submitList(list)
    }

    fun createSpanSizeLookup(spanCount: Int) = GallerySpanSizeLookup(spanCount)

    inner class GallerySpanSizeLookup(var spanCount: Int) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (getItem(position)) {
                is GalleryDay -> spanCount
                is GalleryPhoto -> 1
            }
        }
    }
}