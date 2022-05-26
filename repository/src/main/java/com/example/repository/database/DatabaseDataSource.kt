package com.example.repository.database

import com.example.repository.database.model.Album
import com.example.repository.database.model.AlbumWithPhotos
import com.example.repository.database.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class DatabaseDataSource(private val database: AppDatabase) : PersistentDataSource {

    override suspend fun saveAlbum(vararg albums: Album): List<Long> = withContext(Dispatchers.IO) {
        database.albumDao().insert(*albums)
    }

    override suspend fun savePhoto(vararg photos: Photo): List<Long> = withContext(Dispatchers.IO) {
        database.photoDao().insert(*photos)
    }

    override fun observeAlbumWithPhotosByUri(uri: String): Flow<AlbumWithPhotos?> {
        return database.albumDao().observeAlbumWithPhotosByUri(uri)
    }

    override fun observePhoto(id: String): Flow<Photo?> {
        return database.photoDao().observePhoto(id)
    }
}