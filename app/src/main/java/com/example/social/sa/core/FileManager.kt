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
import androidx.core.database.getLongOrNull
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
        return withContext(dispatcherProvider.io){
            val getType = contentResolver.getType(uri)!!
            val getMediaDate = getDateMedia(uri,contentResolver)
            val getMediaID = getMediaID(uri,contentResolver)
            if (getType.contains("video")){
                val duration = getVideoDuration(uri,contentResolver)
                MediaType.Video(getMediaID,getMediaDate,uri.toString(),duration.toLong())
            }else {
                MediaType.Image(getMediaID,uri.toString(),getMediaDate)
            }
        }
    }

    private fun getMediaID(uri: Uri,contentResolver: ContentResolver):Long{
        val cursor = contentResolver.query(uri, arrayOf(
            MediaStore.Images.Media._ID,
        ), null, null, null)
            ?.use {
                it.moveToFirst()
                val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
                it.getLongOrNull(idIndex) ?: uri.lastPathSegment!!.toLong()
            }
        return cursor!!
    }
    private fun getDateMedia(uri: Uri,contentResolver: ContentResolver):Int{
        val cursor = contentResolver.query(uri, null, null, null, null)
            ?.use {
                it.moveToFirst()
                val dateIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                dateIndex
            }
        return cursor!!
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

                    listImage.add(MediaType.Image(id,contentUri.toString(),date))
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
                    listImage.add(MediaType.Video(idVideo,dateVideo,contentUri.toString(),durationVideo))
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

sealed class MediaType(open val id:Long,open val uri:String,open val date :Int){
    data class Video(override val id: Long, override val date:Int, override val uri:String, val duration :Long):MediaType(id,uri,date)
    data class Image(override val id: Long, override val uri:String, override val date: Int):MediaType(id,uri,date)
}