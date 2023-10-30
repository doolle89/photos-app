package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.ui.screen.fullscreen.FullscreenPhotoCollectionScreen
import com.example.ui.screen.timeline.TimelinePhotoCollectionScreen
import com.example.ui.util.VoyagerSwipeBackContent
import com.example.ui.util.shouldUseSwipeBack


private object MainScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        TimelinePhotoCollectionScreen(
            onPhotoClick = { photoId ->
                navigator.push(FullscreenPhotoCollectionScreen(photoId))
            }
        ).Content()
    }
}

@Composable
fun Navigation() {
    Navigator(
        screen = MainScreen,
    ) { navigator ->
        val supportSwipeBack = remember { shouldUseSwipeBack }
        if(supportSwipeBack) {
            VoyagerSwipeBackContent(navigator)
        } else {
            SlideTransition(navigator)
            //CurrentScreen()
        }
    }
}