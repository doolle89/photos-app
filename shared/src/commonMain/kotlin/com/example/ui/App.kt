package com.example.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.ui.screen.timeline.GalleryScreen
import com.example.ui.view.AsyncImageConfiguration


@Composable
fun App() {
    MaterialTheme {
        AsyncImageConfiguration.CompositionLocalProvider() {
            Navigator(GalleryScreen) { navigator ->
                CurrentScreen()
            }
        }
    }
}