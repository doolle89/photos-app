package com.example.data.local
//
//import androidx.room.*
//import com.example.repository.database.model.Album
//import com.example.repository.database.model.AlbumWithPhotos
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface AlbumDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(vararg albums: Album): List<Long>
//
//    @Delete
//    suspend fun delete(album: Album): Int
//
//    @Query("SELECT * FROM album")
//    fun observeAll(): Flow<List<Album>>
//
//    @Query("SELECT * FROM album WHERE id = :id")
//    fun observeAlbum(id: String): Flow<Album?>
//
//    @Transaction
//    @Query("SELECT * FROM album WHERE uri = :id")
//    fun observeAlbumWithPhotosById(id: String): Flow<AlbumWithPhotos?>
//
//    @Transaction
//    @Query("SELECT * FROM album WHERE uri = :uri")
//    fun observeAlbumWithPhotosByUri(uri: String): Flow<AlbumWithPhotos?>
//}