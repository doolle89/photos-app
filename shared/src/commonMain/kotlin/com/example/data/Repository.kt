package com.example.data

import com.example.data.local.LocalDataSource
import com.example.data.model.Album
import com.example.data.model.Photo
import com.example.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchAlbum(uri: String): Result<Album>
    fun observeAlbum(uri: String): Flow<Album?>
    fun observeAlbum1(uri: String): Flow<Album?>
    fun observePhoto(id: String): Flow<Photo?>

    companion object {
        fun getInstance(): Repository {
            val apiDataSource: RemoteDataSource = RemoteDataSource.getInstance()
            val localDataSource: LocalDataSource = LocalDataSource.getInstance()
            return RepositoryImpl(apiDataSource, localDataSource)
        }
    }
}