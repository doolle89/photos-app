package com.example.repository.api

import com.example.repository.api.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ApiService : RemoteDataSource {

    private val jottaService = ApiInterface.create()

    override suspend fun getAlbum(shareUri: String): Album = withContext(Dispatchers.IO) {
        jottaService.getAlbum(shareUri)
    }
}