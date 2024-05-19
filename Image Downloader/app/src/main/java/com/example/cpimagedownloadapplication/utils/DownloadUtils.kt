package com.example.cpimagedownloadapplication.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cpimagedownloadapplication.R
import com.example.cpimagedownloadapplication.constants.Constants.Companion.CHANNEL_ID
import com.example.cpimagedownloadapplication.constants.Constants.Companion.NOTIFICATION_ID
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.io.OutputStream


object DownloadUtils {

    private var currentDownloadCall: Call<ResponseBody>? = null

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
                                "Image downloaded successfully", 100
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
                if (!call.isCanceled) {
                    onComplete(false)
                    showNotification(context, "Download Failed", "Failed to download image")
                }
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
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "downloaded_image.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri ?: return null

        return try {
            val inputStream: InputStream = body.byteStream()
            val outputStream: OutputStream? = resolver.openOutputStream(uri)

            val fileSize = body.contentLength()
            val buffer = ByteArray(4096)
            var totalBytesRead: Long = 0

            outputStream.use { output ->
                while (true) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    totalBytesRead += bytesRead
                    output?.write(buffer, 0, bytesRead)

                    val progress = (totalBytesRead * 100 / fileSize).toInt()
                    onProgressUpdate(progress)
                    showNotification(context, "Downloading...", "$progress%", progress)

                    if (currentDownloadCall?.isCanceled == true) {
                        onCancel()
                        return null
                    }
                }
                output?.flush()
            }

            uri.toString()
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

    private fun showNotification(context: Context, title: String, text: String, progress: Int = 0) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.download)
            .setContentTitle(title)
            .setContentText(if (progress == 100) "Image downloaded successfully" else text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // If progress is not 100%, set progress bar
        if (progress < 100) {
            builder.setProgress(100, progress, false)
        } else {
            // If progress is 100%, remove progress bar
            builder.setProgress(0, 0, false)
        }
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