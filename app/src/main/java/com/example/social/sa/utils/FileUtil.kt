package com.example.social.sa.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.File

fun saveTempFile(name:String,bitmap: Bitmap):String{
    val tempFile = File.createTempFile(name, ".jpeg")
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, tempFile.outputStream())
    return tempFile.absolutePath
}

fun saveExternalStorage(context: Context,name: String,bitmap: Bitmap){
    val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME,name)
        put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
        put(MediaStore.Images.Media.WIDTH,bitmap.width)
        put(MediaStore.Images.Media.HEIGHT,bitmap.height)
    }
    context.contentResolver.insert(imageCollection,contentValues)?.also {uri->
        context.contentResolver.openOutputStream(uri).use { outputStream->
            bitmap.compress(Bitmap.CompressFormat.JPEG,95,outputStream!!)
        }
    }
}