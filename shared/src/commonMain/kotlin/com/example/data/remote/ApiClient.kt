package com.example.data.remote

import com.example.data.remote.model.AlbumApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getAlbum(shareUri: String): AlbumApiModel {
        val response = client.get("https://api.jottacloud.com/photos/v1/public/$shareUri")
        return response.body()
    }

    companion object {
        fun create(): ApiClient = ApiClient()
    }
}