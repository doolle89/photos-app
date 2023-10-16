package com.example.data.remote

import com.example.data.remote.model.AlbumApiModel

interface RemoteDataSource {
    suspend fun getAlbum(shareUri: String): AlbumApiModel

    companion object {
        fun getInstance(): RemoteDataSource {
            return ApiService()
        }
    }
}