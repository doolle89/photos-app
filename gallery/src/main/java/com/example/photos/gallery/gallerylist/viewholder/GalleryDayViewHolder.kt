package com.example.photos.gallery.gallerylist.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.photos.gallery.databinding.ItemGalleryDayBinding
import com.example.photos.gallery.model.GalleryDay

class GalleryDayViewHolder(private val binding: ItemGalleryDayBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GalleryDay) {
        binding.headerTitle.text = item.title
    }
}