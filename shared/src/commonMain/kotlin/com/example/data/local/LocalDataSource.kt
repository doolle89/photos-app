package com.example.data.local


import com.example.data.local.database.AlbumDbModel
import com.example.data.local.database.DatabaseManager
import com.example.data.local.database.PhotoDbModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveAlbum(album: AlbumDbModel)
    suspend fun saveAlbums(albums: List<AlbumDbModel>)
    suspend fun getAllAlbums(): List<AlbumDbModel>
    suspend fun getAlbumByUri(uri: String): AlbumDbModel?
    fun observeAlbumByUri(uri: String): Flow<AlbumDbModel?>
    suspend fun savePhoto(photo: PhotoDbModel)
    suspend fun savePhotos(photos: List<PhotoDbModel>)
    suspend fun addPhotoToAlbum(albumId: String, photoId: String)
    suspend fun addPhotosToAlbum(albumId: String, photos: List<PhotoDbModel>)
    suspend fun getAllPhotos(): List<PhotoDbModel>
    fun observePhoto(id: String): Flow<PhotoDbModel?>
    suspend fun getPhotosInAlbum(albumId: String): List<PhotoDbModel>
    fun observePhotosInAlbum(albumId: String): Flow<List<PhotoDbModel>>

    companion object {
        fun getInstance(): LocalDataSource {
            return DatabaseDataSource(DatabaseManager.database)
        }
    }
}