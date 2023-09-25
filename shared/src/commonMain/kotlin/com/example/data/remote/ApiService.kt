package com.example.data.remote

import com.example.data.remote.model.Album
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

//internal class ApiService : RemoteDataSource {
//
//    private val jottaService = ApiInterface.create()
//
//    override suspend fun getAlbum(shareUri: String): Album = withContext(Dispatchers.IO) {
//        jottaService.getAlbum(shareUri)
//    }
//}

class ApiService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {

    private val apiClient = ApiClient.create()

    override suspend fun getAlbum(shareUri: String): Album = withContext(dispatcher) {
        apiClient.getAlbum(shareUri)
    }
}