package com.example.etmovieexplorer.utils

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.network.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

object DownloadUtils {

    fun downloadImage(
        context: Context,
        imageUrl: String,
        onComplete: (Boolean) -> Unit,
    ): Call<ResponseBody> {

        val call = ApiClient.init().downloadImage(imageUrl)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        saveImageToGallery(context, responseBody)
                    }
                } else {
                    onComplete(false)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (!call.isCanceled) {
                    onComplete(false)
                }
            }
        })
        return call
    }

    private fun saveImageToGallery(
        context: Context,
        body: ResponseBody,
    ): String? {

        val contentResolver = context.contentResolver
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val mimeType = "image/jpeg"
        val directory =
            "${Environment.DIRECTORY_PICTURES}/${context.resources.getString(R.string.app_name)}"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, directory)
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri ?: return null

        return try {
            var totalBytesRead: Long = 0
            val buffer = ByteArray(4096)
            val inputStream = body.byteStream()
            val outputStream = contentResolver.openOutputStream(uri)

            outputStream.use { output ->
                while (true) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    totalBytesRead += bytesRead
                    output?.write(buffer, 0, bytesRead)
                }
                output?.flush()
                Toast.makeText(context, context.getString(R.string.image_download_successful), Toast.LENGTH_SHORT).show()
            }
            uri.toString()
        } catch (e: IOException) {
            null
        }
    }
}