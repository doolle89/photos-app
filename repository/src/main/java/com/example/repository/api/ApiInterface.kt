package com.example.repository.api

import com.example.repository.api.model.Album
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("public/{public_share_uri}")
    suspend fun getAlbum(@Path("public_share_uri") shareUri: String): Album

    companion object {
        fun create(): ApiInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.jottacloud.com/photos/v1/")
                .build()
                .create(ApiInterface::class.java)
        }
    }
}