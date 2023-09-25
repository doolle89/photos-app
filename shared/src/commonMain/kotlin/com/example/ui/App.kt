@file:OptIn(ExperimentalResourceApi::class)

package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.data.RepositoryTemp
import com.example.data.remote.model.Album
import com.example.data.remote.model.Photo
import com.example.ui.screen.GalleryScreen
import com.example.ui.view.AsyncImage
import getPlatformName
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun App() {
    MaterialTheme {
        GalleryScreen()
    }
}

@Composable
fun HelloWorld() {
    var greetingText by remember { mutableStateOf("Hello, World!") }
    var showImage by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }
        ) {
            Text(greetingText)
        }
        val coroutineScope = rememberCoroutineScope()
        var albumText by remember { mutableStateOf("") }
        Button(
            onClick = {
                coroutineScope.launch {
                    val repository = RepositoryTemp()
                    val album = repository.getAlbum(AlbumUrls.albumUrl)
                    albumText = album.toString()
                }
            }
        ) {
            Text("Load album")
        }
        Text(albumText)
        AnimatedVisibility(showImage) {
            Image(
                painterResource("compose-multiplatform.xml"),
                null
            )
        }
    }
}