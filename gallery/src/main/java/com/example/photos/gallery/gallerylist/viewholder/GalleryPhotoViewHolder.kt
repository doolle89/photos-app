package com.example.photos.gallery.gallerylist.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.photos.gallery.R
import com.example.photos.gallery.databinding.ItemGalleryPhotoBinding
import com.example.photos.gallery.model.GalleryItem
import com.example.photos.gallery.model.GalleryPhoto

class GalleryPhotoViewHolder(
    private val binding: ItemGalleryPhotoBinding,
    private val onPhotoLoadedListener: ((GalleryItem, Boolean) -> Unit)? = null,
    onItemClickListener: ((View, GalleryItem) -> Unit)? = null
)
    : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: GalleryItem

    init {
        itemView.setOnClickListener { view ->
            onItemClickListener?.invoke(view, item)
        }
    }

    fun bind(item: GalleryPhoto) {
        this.item = item
        binding.imageView.isClickable = false
        ViewCompat.setTransitionName(binding.imageView, item.id)
        Glide.with(binding.imageView)
            .load(item.thumbnailUrlSmall)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    onPhotoLoadedListener?.invoke(item, false)
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    onPhotoLoadedListener?.invoke(item, true)
                    binding.imageView.isClickable = true
                    return false
                }
            })
            .into(binding.imageView)
    }

}