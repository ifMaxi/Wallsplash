package com.maxidev.wallsplash.common.wallpaper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.maxidev.wallsplash.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

private const val CHANNEL_ID = "wallpaper_channel"
private const val NOTIFICATION_ID = 1

suspend fun setWallpaper(context: Context, url: String) {
    withContext(Dispatchers.IO) {
        try {
            val imageUrl = URL(url)
            val bitmap = BitmapFactory.decodeStream(imageUrl.openStream())

            if (bitmap != null) {
                val wallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                sendNotification(context)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
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