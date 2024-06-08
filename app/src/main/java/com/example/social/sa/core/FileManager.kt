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

//    suspend fun getMedia(uri:Uri):MediaType{
//        return withContext(Dispatchers.IO){
//            val getType = contentResolver.getType(uri)!!
//            if (getType.contains("video")){
//                val duration = getVideoDuration(uri,contentResolver)
//                MediaType.Video(duration,uri.toString())
//            }else {
//                MediaType.Image(uri.toString())
//            }
//        }
//    }

    private fun getVideoDuration(uri: Uri,contentResolver: ContentResolver): Int {
        val cursor = contentResolver.query(uri, null, null, null, null)
            ?.use {
                it.moveToFirst()
                val durationIndex = it.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                durationIndex
            }
        return cursor!!
    }

    suspend fun loadFilesExternalStorage(): List<MediaType> {
        return withContext(dispatcherProvider.default) {
            val collectionImage =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val progection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED
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
            val listImage = mutableListOf<MediaType>()
            contentResolver.query(
                collectionImage,
                progection,
                bundle, null
            ).use {
                val columnId = it!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateColumn = it!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                while (it.moveToNext()) {
                    val id = it.getLong(columnId)
                    val date = it.getInt(dateColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    listImage.add(MediaType.Image(contentUri.toString(),date))
                }
            }
            val collectionVideo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val progectionVideo = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION,MediaStore.Video.Media.DATE_ADDED
            )
            val bundleVideo = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, 10)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Video.Media.DATE_ADDED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }
            contentResolver.query(
                collectionVideo,
                progectionVideo,
                bundleVideo, null
            ).use{
                val columnIdVideo = it!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val columnDuration = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val ColumnDateVideo = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                while (it.moveToNext()){
                    val idVideo = it.getLong(columnIdVideo)
                    val durationVideo = it.getLong(columnDuration)
                    val contentUri = ContentUris.withAppendedId(collectionVideo,idVideo)
                    val dateVideo = it.getInt(ColumnDateVideo)
                    listImage.add(MediaType.Video(dateVideo,contentUri.toString(),durationVideo))
                }
            }
            listImage.sortedByDescending {
                it.date
            }
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

sealed class MediaType(open val uri:String,open val date :Int){
    data class Video(override val date:Int, override val uri:String,val duration :Long):MediaType(uri,date)
    data class Image(override val uri:String, override val date: Int):MediaType(uri,date)
}