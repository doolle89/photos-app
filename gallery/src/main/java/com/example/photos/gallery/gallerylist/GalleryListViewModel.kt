package com.example.photos.gallery.gallerylist

import android.app.Application
import androidx.lifecycle.*
import com.example.photos.gallery.R
import com.example.photos.gallery.model.GalleryDay
import com.example.photos.gallery.model.GalleryItem
import com.example.photos.gallery.model.StringResource
import com.example.photos.gallery.model.toGalleryDays
import com.example.photos.gallery.utils.AlbumUrls
import com.example.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GalleryListViewModel(application: Application) : AndroidViewModel(application) {

    private val albumUrl = AlbumUrls.albumUrl
    private val repository : Repository = Repository.getInstance(application)

    private val galleryItemsLiveData = repository.observeAlbum(albumUrl).map { albumWithPhotos ->
        flatGalleryItems(albumWithPhotos?.toGalleryDays())
    }.flowOn(Dispatchers.Default).asLiveData()

    private val _uiState = MediatorLiveData<GalleryListUiState>().apply {
        value = GalleryListUiState()
        addSource(galleryItemsLiveData) {
            value = value?.copy(galleryItems = it)
        }
    }
    val uiState: LiveData<GalleryListUiState> = _uiState

    init {
        fetchAlbum()
    }

    fun fetchAlbum() {
        viewModelScope.launch {
            try {
                setLoadingState(true)
                repository.fetchAlbum(albumUrl)
            } catch (e: Exception) {
                setErrorMessage(StringResource(R.string.error_network))
            } finally {
                setLoadingState(false)
            }
        }
    }

    fun getItemPositionInGalleryItems(id: String): Int {
        galleryItemsLiveData.value?.forEachIndexed { i, item ->
            if (id == item.id) {
                return i
            }
        }
        return -1
    }

    fun setErrorMessageShown(errorMessage: StringResource) {
        _uiState.value?.let {
            _uiState.value = it.copy(errorMessages = it.errorMessages.filterNot { it == errorMessage })
        }
    }

    private fun setErrorMessage(errorMessage: StringResource) {
        _uiState.value?.let {
            _uiState.value = it.copy(errorMessages = it.errorMessages + errorMessage)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.value = _uiState.value?.copy(isLoading = isLoading)
    }

    private fun flatGalleryItems(galleryDays: List<GalleryDay>?): List<GalleryItem> {
        val result = mutableListOf<GalleryItem>()
        galleryDays?.forEach { galleryDay ->
            result.add(galleryDay)
            galleryDay.photos.forEach { photo ->
                result.add(photo)
            }
        }
        return result
    }
}