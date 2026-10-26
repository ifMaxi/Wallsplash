package com.maxidev.wallsplash.wallpaper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.Context
import androidx.core.app.NotificationCompat
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.maxidev.wallsplash.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val CHANNEL_ID = "wallpaper_channel"
private const val NOTIFICATION_ID = 1

object WallpaperHelper {

    suspend fun applyWallpaper(
        context: Context,
        imageUrl: String,
        target: WallpaperTarget
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = loader.execute(request)
            if (result !is SuccessResult) return@withContext false

            val bitmap = result.image.toBitmap()

            val wallpaperManager = WallpaperManager.getInstance(context)

            val flag = when (target) {
                WallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                WallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                WallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            }
            wallpaperManager.setBitmap(bitmap, null, true, flag)
            sendNotification(context)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

enum class WallpaperTarget(val label: String) {
    HOME_SCREEN("Home screen"),
    LOCK_SCREEN("Lock screen"),
    BOTH("Both")
}

private fun sendNotification(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

    val name = "Wallpaper status"
    val descriptionText = "Notifications for wallpaper set status"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(
        CHANNEL_ID, name, importance
    ).apply { description = descriptionText }

    notificationManager.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setContentTitle("Wallpaper updated")
        .setContentText("New wallpaper set successfully.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(NOTIFICATION_ID, builder)
}