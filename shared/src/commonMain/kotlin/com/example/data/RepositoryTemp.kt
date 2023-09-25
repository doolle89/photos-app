package com.example.data

import com.example.data.remote.RemoteDataSource
import com.example.data.remote.model.Album

class RepositoryTemp(
    private val remoteDataSource: RemoteDataSource = RemoteDataSource.getInstance()
) {
    suspend fun getAlbum(shareUri: String): Album {
        return remoteDataSource.getAlbum(shareUri)
    }
}