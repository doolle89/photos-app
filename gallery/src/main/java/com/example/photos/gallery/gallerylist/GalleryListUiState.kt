package com.example.photos.gallery.gallerylist

import com.example.photos.gallery.model.GalleryItem
import com.example.photos.gallery.model.StringResource

data class GalleryListUiState(
    val galleryItems: List<GalleryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<StringResource> = emptyList()
 )
