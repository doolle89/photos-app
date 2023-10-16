package com.example.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.kamel.core.config.DefaultCacheSize
import io.kamel.core.config.DefaultHttpCacheSize
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.fileFetcher
import io.kamel.core.config.httpFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.imageBitmapDecoder
import io.kamel.image.config.imageVectorDecoder
import io.kamel.image.config.svgDecoder
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.isSuccess


actual object AsyncImageConfiguration {
    @Composable
    actual fun CompositionLocalProvider(
        block: @Composable () -> Unit
    ) {
        val customKamelConfig = KamelConfig {
            // Copies the default implementation if needed
            takeFrom(KamelConfig.Default)
            // 100 by default
            imageBitmapCacheSize = 500
            // 100 by default
            imageVectorCacheSize = 300
            // 100 by default
            svgCacheSize = 50
            // Sets the number of images to cache
            imageBitmapCacheSize = DefaultCacheSize
            // adds an ImageBitmapDecoder
            imageBitmapDecoder()
            // adds an ImageVectorDecoder (XML)
            imageVectorDecoder()
            // adds an SvgDecoder (SVG)
            svgDecoder()
            // adds a FileFetcher
            fileFetcher()
            // Configures Ktor HttpClient
            httpFetcher {
                // Disc cache
                httpCache(100 * DefaultHttpCacheSize)
                install(HttpRequestRetry) {
                    maxRetries = 3
                    retryIf { httpRequest, httpResponse ->
                        !httpResponse.status.isSuccess()
                    }
                }
                // Requires adding "io.ktor:ktor-client-logging:$ktor_version"
                Logging {
                    level = LogLevel.INFO
                    logger = Logger.SIMPLE
                }
            }
            // more functionality available.
        }

        CompositionLocalProvider(LocalKamelConfig provides customKamelConfig) {
            block()
        }
    }
}