package com.example.photos.gallery.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.photos.gallery.model.GalleryItem

object GalleryItemDiffUtilCallback : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem::class == newItem::class && oldItem.id == newItem.id
    }
}