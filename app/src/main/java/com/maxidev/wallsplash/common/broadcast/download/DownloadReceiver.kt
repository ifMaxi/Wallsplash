package com.maxidev.wallsplash.common.broadcast.download

import android.app.DownloadManager
import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.app.DownloadManager.Request.NETWORK_MOBILE
import android.app.DownloadManager.Request.NETWORK_WIFI
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri

/**
 * Broadcast receiver that handles the download complete event.
 */
class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, -1L)

            if (id != -1L) {
                Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

interface Downloader {

    fun download(url: String): Long
}

/**
 * System service that handles long-running HTTP downloads using Download Manager.
 *
 * @param context allows access to specific system resources.
 */
class AndroidDownloader(context: Context) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    /**
     * Downloads a file from the given URL and saves it to the given path.
     *
     * @param url address of the link where the download will be made.
     */
    override fun download(url: String): Long {
        val fileName = url.toUri().lastPathSegment ?: "${System.currentTimeMillis()}.jpg"
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Wallsplash downloader")
            .setMimeType("image/jpg")
            .setRequiresCharging(false)
            .setAllowedNetworkTypes(NETWORK_WIFI or NETWORK_MOBILE)
            .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        return downloadManager.enqueue(request)
    }
}