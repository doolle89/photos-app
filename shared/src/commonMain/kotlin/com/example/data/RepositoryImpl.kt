@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.data

import com.example.data.local.LocalDataSource
import com.example.data.model.Album
import com.example.data.model.Photo
import com.example.data.remote.RemoteDataSource
import com.example.data.utils.toAlbum
import com.example.data.utils.toDatabaseModel
import com.example.data.utils.toPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun fetchAlbum(uri: String): Result<Album> = withContext(Dispatchers.Default) {
        try {
            val albumApiModel = remoteDataSource.getAlbum(uri)
            val albumDatabaseModel = albumApiModel.toDatabaseModel(uri)
            val photosDatabaseModel = albumApiModel.photos?.map { it.toDatabaseModel() } ?: emptyList()
            localDataSource.saveAlbum(albumDatabaseModel)
            localDataSource.savePhotos(photosDatabaseModel)
            localDataSource.addPhotosToAlbum(albumDatabaseModel.id, photosDatabaseModel)
            Result.success(albumDatabaseModel.toAlbum(photosDatabaseModel))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeAlbum(uri: String): Flow<Album?> {
        return localDataSource.observeAlbumByUri(uri).flatMapLatest { albumDbModel ->
            if (albumDbModel != null) {
                localDataSource.observePhotosInAlbum(albumDbModel.id).flatMapLatest { photosDbModel ->
                    flowOf(albumDbModel.toAlbum(photosDbModel))
                }
            } else {
                emptyFlow()
            }
        }
    }

    override fun observeAlbum1(uri: String): Flow<Album?> {
        return localDataSource.observeAlbumByUri(uri).map { albumDbModel ->
            if (albumDbModel != null) {
                val photos = localDataSource.getAllPhotos()
                albumDbModel.toAlbum(photos)
            } else {
                null
            }
        }
    }

    override fun observePhoto(id: String): Flow<Photo?> {
        return localDataSource.observePhoto(id).map { photoDbModel -> photoDbModel?.toPhoto() }
    }
}