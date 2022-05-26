package com.example.photos.gallery.gallerypreview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.photos.gallery.model.toGalleryAlbum
import com.example.photos.gallery.utils.AlbumUrls
import com.example.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GalleryPreviewViewModel(application: Application) : AndroidViewModel(application) {

    private val albumUrl = AlbumUrls.albumUrl

    private val repository : Repository = Repository.getInstance(application)

    val galleryPhotosLiveData = repository.observeAlbum(albumUrl).map { albumWithPhotos ->
        albumWithPhotos?.toGalleryAlbum()?.photos
    }.flowOn(Dispatchers.Default).asLiveData()

    fun getPositionInGalleryPhotos(id: String): Int {
        galleryPhotosLiveData.value?.forEachIndexed { i, photo ->
            if (id == photo.id) {
                return i
            }
        }
        return -1
    }
}