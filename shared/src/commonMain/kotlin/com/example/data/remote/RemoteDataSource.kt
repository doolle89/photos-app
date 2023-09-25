package com.example.data.remote

import com.example.data.remote.model.Album

interface RemoteDataSource {
    suspend fun getAlbum(shareUri: String): Album

    companion object {
        fun getInstance(): RemoteDataSource {
            return ApiService()
        }
    }
}