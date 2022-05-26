package com.example.repository

import com.example.repository.database.PersistentDataSource
import com.example.repository.database.model.AlbumWithPhotos
import com.example.repository.utils.toDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import com.example.repository.api.RemoteDataSource as ApiDataSource
import com.example.repository.api.model.Album as AlbumApiModel
import com.example.repository.database.model.Photo as PhotoDatabaseModel

internal class DataRepository(
    private val apiDataSource: ApiDataSource,
    private val persistentDataSource: PersistentDataSource
) : Repository {

    override suspend fun fetchAlbum(uri: String) = withContext(Dispatchers.Default) {
        val albumApiModel = getAlbum(uri)
        val albumDatabaseModel = albumApiModel.toDatabaseModel(uri)
        val photosDatabaseModel = albumApiModel.photos?.map { it.toDatabaseModel(albumDatabaseModel.id) } ?: emptyList()
        persistentDataSource.saveAlbum(albumDatabaseModel)
        persistentDataSource.savePhoto(*photosDatabaseModel.toTypedArray())
        return@withContext
    }

    override fun observeAlbum(uri: String): Flow<AlbumWithPhotos?> {
        return persistentDataSource.observeAlbumWithPhotosByUri(uri)
    }

    override fun observePhoto(id: String): Flow<PhotoDatabaseModel?> {
        return persistentDataSource.observePhoto(id)
    }

    private suspend fun getAlbum(uri: String): AlbumApiModel {
        return apiDataSource.getAlbum(uri)
    }
}