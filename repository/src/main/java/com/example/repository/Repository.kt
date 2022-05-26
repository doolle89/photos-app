package com.example.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.repository.api.RemoteDataSource
import com.example.repository.database.PersistentDataSource
import com.example.repository.database.model.AlbumWithPhotos
import com.example.repository.database.model.Photo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchAlbum(uri: String)
    fun observeAlbum(uri: String): Flow<AlbumWithPhotos?>
    fun observePhoto(id: String): Flow<Photo?>

    companion object {
        fun getInstance(context: Context): Repository {
            val apiDataSource: RemoteDataSource = RemoteDataSource.getInstance()
            val persistentDataSource: PersistentDataSource = PersistentDataSource.getInstance(context)
            return DataRepository(apiDataSource, persistentDataSource)
        }
    }
}