package com.example.data

import com.example.data.remote.RemoteDataSource
import com.example.data.remote.model.AlbumApiModel

class RepositoryTemp(
    private val remoteDataSource: RemoteDataSource = RemoteDataSource.getInstance()
) {
    suspend fun getAlbum(shareUri: String): AlbumApiModel {
        return remoteDataSource.getAlbum(shareUri)
    }
}