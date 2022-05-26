package com.example.photos.gallery.photopreview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.photos.gallery.model.toGalleryPhoto
import com.example.repository.Repository
import kotlinx.coroutines.flow.map

class PhotoPreviewViewModel(application: Application,
                            private val repository : Repository) : AndroidViewModel(application) {

    constructor(application: Application) : this(application, Repository.getInstance(application))

    private val photoIdLiveData = MutableLiveData<String>()

    val galleryPhotoLiveData = photoIdLiveData.switchMap { photoId ->
        repository.observePhoto(photoId).map { photo ->
            photo?.toGalleryPhoto()
        }.asLiveData()
    }

    fun setGalleryPhotoId(photoId: String) {
        photoIdLiveData.value = photoId
    }
}