package com.maxidev.wallsplash

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.memoryCacheMaxSizePercentWhileInBackground
import coil3.request.CachePolicy
import coil3.request.allowHardware
import coil3.request.allowRgb565
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

@HiltAndroidApp
class MyApp: Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader {

        val cachePolicy = CachePolicy.ENABLED
        val fetcher = ImageLoader.Builder(context)
            .crossfade(true)
            .allowRgb565(true)
            .allowHardware(true)
            .memoryCacheMaxSizePercentWhileInBackground(0.5)
            .components { GifDecoder.Factory() }
            .memoryCachePolicy(cachePolicy)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context = context, 0.03)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(cachePolicy)
            .diskCache {
                val fileName = "image_cache"
                val fileSystem = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
                val maxSizeInBytes = 1024L * 1024 * 1024 // 512 mb

                DiskCache.Builder()
                    .directory(fileSystem / fileName)
                    .maxSizeBytes(maxSizeInBytes)
                    .build()
            }
            .networkCachePolicy(cachePolicy)
            .coroutineContext(Dispatchers.IO)

        return fetcher.build()
    }
}