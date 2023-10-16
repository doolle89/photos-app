package com.example.data.local

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.data.local.database.AlbumDbModel
import com.example.data.local.database.AppDatabase
import com.example.data.local.database.PhotoDbModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


internal class DatabaseDataSource(
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override suspend fun saveAlbum(album: AlbumDbModel) = withContext(dispatcher) {
        database.albumDaoQueries.insert(
            id = album.id,
            uri = album.uri,
            username = album.username,
            title = album.title,
            description = album.description,
            createdDate = album.createdDate,
            lastModified = album.lastModified,
            meta = album.meta,
            coverPhoto = album.coverPhoto,
            total = album.total
        )
    }

    override suspend fun saveAlbums(albums: List<AlbumDbModel>) {
        database.albumDaoQueries.transaction(true)  {
            albums.forEach { saveAlbum(it) }
        }
    }

    override suspend fun getAllAlbums(): List<AlbumDbModel> {
        return database.albumDaoQueries.selectAll().awaitAsList()
    }

    override suspend fun getAlbumByUri(uri: String): AlbumDbModel? {
        return database.albumDaoQueries.selectAlbumByUri(uri).awaitAsOneOrNull()
    }

    override fun observeAlbumByUri(uri: String): Flow<AlbumDbModel?> {
        return database.albumDaoQueries.selectAlbumByUri(uri).asFlow().mapToOneOrNull(dispatcher)
    }

    override suspend fun savePhoto(photo: PhotoDbModel) = withContext(dispatcher) {
        database.photoDaoQueries.insert(
            id = photo.id,
            title = photo.title,
            description = photo.description,
            mimeType = photo.mimeType,
            capturedDate = photo.capturedDate,
            content = photo.content,
            width = photo.width,
            height = photo.height,
            orientation = photo.orientation,
            fileUrl = photo.fileUrl,
            thumbnailUrl = photo.thumbnailUrl,
            videoUrl = photo.videoUrl,
            livePhotoUrl = photo.livePhotoUrl,
            livePhotoType = photo.livePhotoType,
            hidden = photo.hidden,
            excluded = photo.excluded,
        )
    }

    override suspend fun savePhotos(photos: List<PhotoDbModel>) {
        //database.photoDaoQueries.transaction  {
            photos.forEach { savePhoto(it) }
        //}
    }

    override suspend fun addPhotoToAlbum(albumId: String, photoId: String) {
        database.photoDaoQueries.addPhotoToAlbum(albumId, photoId)
    }

    override suspend fun addPhotosToAlbum(albumId: String, photos: List<PhotoDbModel>) {
        database.photoDaoQueries.transaction(true) {
            photos.forEach { addPhotoToAlbum(albumId, it.id) }
        }
    }

    override suspend fun getAllPhotos(): List<PhotoDbModel> {
        return database.photoDaoQueries.selectAll().awaitAsList()
    }

    override fun observePhoto(id: String): Flow<PhotoDbModel?> {
        return database.photoDaoQueries.selectPhoto(id).asFlow().mapToOneOrNull(dispatcher)
    }

    override suspend fun getPhotosInAlbum(albumId: String): List<PhotoDbModel> {
        return database.photoDaoQueries.selectAllPhotosInAlbum(albumId).awaitAsList()
    }

    override fun observePhotosInAlbum(albumId: String): Flow<List<PhotoDbModel>> {
        return database.photoDaoQueries.selectAllPhotosInAlbum(albumId).asFlow().mapToList(dispatcher)
    }
}