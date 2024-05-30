package com.example.social.sa.core

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.decodeBitmap
import com.example.social.sa.Constants
import com.example.social.sa.coroutine.DispatcherProviderImpl
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class FileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val contentResolver = context.contentResolver
    private val quality: Int = 80

    private val dispatcherProvider = DispatcherProviderImpl()

    companion object {
        private const val ONE_MB: Long = 1024 * 1024
        private const val FIFTY_MB: Long = ONE_MB * 50
    }

    suspend fun getMedia(uri:Uri):MediaType{
        return withContext(Dispatchers.IO){
            val getType = contentResolver.getType(uri)!!
            if (getType.contains("video")){
                val duration = getVideoDuration(uri,contentResolver)
                MediaType.Video(duration,uri.toString())
            }else {
                MediaType.Image(uri.toString())
            }
        }
    }

    private fun getVideoDuration(uri: Uri,contentResolver: ContentResolver): Int {
        val cursor = contentResolver.query(uri, null, null, null, null)
            ?.use {
                it.moveToFirst()
                val durationIndex = it.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                durationIndex
            }
        return cursor!!
    }

    suspend fun loadFilesExternalStorage(): List<String> {
        return withContext(dispatcherProvider.default) {
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val progection = arrayOf(
                MediaStore.Images.Media._ID,
            )
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, 10)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Images.Media.DATE_ADDED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }
            val listImage = mutableListOf<String>()
            contentResolver.query(
                collection,
                progection,
                bundle, null
            ).use {
                val columnId = it!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (it.moveToNext()) {
                    val id = it.getLong(columnId)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    listImage.add(contentUri.toString())
                }
            }
            listImage
        }
    }

    val formatImage: Bitmap.CompressFormat =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.JPEG
        }

    suspend fun compressImage(imageUri: Uri): ByteArray {
        return withContext(dispatcherProvider.default) {
            val outputStream = ByteArrayOutputStream()
            val btm =
                ImageDecoder.createSource(contentResolver, imageUri).decodeBitmap { info, source ->

                }
            btm.compress(formatImage, quality, outputStream)
            outputStream.toByteArray()
        }
    }
}

sealed class MediaType(open val uri:String){
    data class Video(val duration:Int, override val uri:String):MediaType(uri)
    data class Image(override val uri:String):MediaType(uri)
}