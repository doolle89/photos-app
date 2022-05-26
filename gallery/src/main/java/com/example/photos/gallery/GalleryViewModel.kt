package com.example.photos.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.photos.gallery.model.GalleryPhoto

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    var selectedGalleryPhoto: GalleryPhoto? = null

}