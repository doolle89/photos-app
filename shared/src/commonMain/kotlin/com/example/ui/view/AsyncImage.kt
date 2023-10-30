package com.example.ui.view

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.CacheControl
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job

@Composable
fun AsyncImage(
    url: String,
    contentDescription: String? = "",
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (BoxScope.(Float) -> Unit)? = null,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    animationSpec: FiniteAnimationSpec<Float>? = null
) {
    AsyncImageConfiguration.CompositionLocalProvider {
        KamelImage(
            resource = asyncPainterResource(data = Url(url)) {
                coroutineContext = Job() + Dispatchers.IO

                // Customizes HTTP request
                requestBuilder {
                    cacheControl(CacheControl.MaxAge(10000))
                }
            },
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            onLoading = onLoading,
            onFailure = onFailure,
            contentAlignment = contentAlignment,
            animationSpec = animationSpec
        )
    }
}

expect object AsyncImageConfiguration {
    @Composable
    fun CompositionLocalProvider(
        block: @Composable () -> Unit
    )
}