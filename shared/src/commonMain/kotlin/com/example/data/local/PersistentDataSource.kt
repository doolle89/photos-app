package com.example.data.local
//
//import android.content.Context
//import com.example.repository.database.model.Album
//import com.example.repository.database.model.AlbumWithPhotos
//import com.example.repository.database.model.Photo
//import kotlinx.coroutines.flow.Flow
//
//interface PersistentDataSource {
//    suspend fun saveAlbum(vararg albums: Album): List<Long>
//    suspend fun savePhoto(vararg photos: Photo): List<Long>
//    fun observeAlbumWithPhotosByUri(uri: String): Flow<AlbumWithPhotos?>
//    fun observePhoto(id: String): Flow<Photo?>
//
//    companion object {
//        fun getInstance(context: Context): PersistentDataSource {
//            return DatabaseDataSource(AppDatabase.getInstance(context))
//        }
//    }
//}