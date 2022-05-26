package com.example.repository.api

import com.example.repository.api.model.Album

interface RemoteDataSource {
    suspend fun getAlbum(shareUri: String): Album

    companion object {
        fun getInstance(): RemoteDataSource {
            return ApiService()
        }
    }
}