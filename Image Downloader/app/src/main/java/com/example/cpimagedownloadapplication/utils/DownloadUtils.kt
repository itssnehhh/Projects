package com.example.cpimagedownloadapplication.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cpimagedownloadapplication.R
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object DownloadUtils {

    private const val CHANNEL_ID = "download_channel"
    private const val NOTIFICATION_ID = 1


    fun downloadImage(
        context: Context,
        imageUrl: String,
        onProgressUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit,
        onCancel: () -> Unit,
    ): Call<ResponseBody> {
        createNotificationChannel(context)
        val apiService = com.example.cpimagedownloadapplication.network.ApiClient.init()
        val call = apiService.downloadImage(imageUrl)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val filePath =
                            saveImageToGallery(context, responseBody, onProgressUpdate, onCancel)
                        val success = filePath != null
                        onComplete(success)
                        if (success) {
                            showNotification(
                                context,
                                "Download Complete",
                                "Image downloaded successfully"
                            )
                        } else {
                            showNotification(context, "Download Failed", "Failed to download image")
                        }
                    }
                } else {
                    onComplete(false)
                    showNotification(context, "Download Failed", "Failed to download image")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onComplete(false)
                showNotification(context, "Download Failed", "Failed to download image")
            }
        })
        return call
    }

    private fun saveImageToGallery(
        context: Context,
        body: ResponseBody,
        onProgressUpdate: (Int) -> Unit,
        onCancel: () -> Unit,
    ): String? {
        return try {
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(directory, "downloaded_image.jpg")
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {
                val fileSize = body.contentLength()
                var totalBytesRead: Long = 0
                val buffer = ByteArray(4096)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    totalBytesRead += bytesRead
                    outputStream.write(buffer, 0, bytesRead)

                    val progress = (totalBytesRead * 100 / fileSize).toInt()
                    onProgressUpdate(progress)
                    showNotification(context, "Downloading...", "$progress%")
                }
                outputStream.flush()
                file.absolutePath
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            Log.e("DownloadUtils", "Failed to save image: ${e.message}", e)
            null
        }
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download Channel"
            val descriptionText = "Channel for download notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.download)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}