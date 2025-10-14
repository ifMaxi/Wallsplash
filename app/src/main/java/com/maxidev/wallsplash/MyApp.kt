package com.maxidev.wallsplash

import android.app.Application
import android.os.Build
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.memoryCacheMaxSizePercentWhileInBackground
import coil3.request.CachePolicy
import coil3.request.allowHardware
import coil3.request.allowRgb565
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

@OptIn(ExperimentalCoilApi::class)
@HiltAndroidApp
class MyApp: Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader {

        val cachePolicy = CachePolicy.ENABLED
        val debugLogger = DebugLogger()

        return ImageLoader.Builder(context)
            .crossfade(true)
            .allowRgb565(true)
            .allowHardware(true)
            .memoryCacheMaxSizePercentWhileInBackground(0.5)
            .components {
                when {
                    Build.VERSION.SDK_INT >= 28 -> add(AnimatedImageDecoder.Factory())
                    else -> add(GifDecoder.Factory())
                }
            }
            .memoryCachePolicy(policy = cachePolicy)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context = context, 0.03)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(policy = cachePolicy)
            .diskCache {
                val fileName = "image_cache"
                val fileSystem = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
                val maxSizeInBytes = 1024L * 1024 * 1024 // 512 mb

                DiskCache.Builder()
                    .directory(fileSystem / fileName)
                    .maxSizeBytes(maxSizeInBytes)
                    .build()
            }
            .networkCachePolicy(policy = cachePolicy)
            .coroutineContext(context = Dispatchers.IO)
            .logger(logger = debugLogger)
            .build()
    }
}