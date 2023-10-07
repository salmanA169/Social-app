package com.example.social.sa.core

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.social.sa.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Storage @Inject constructor(
    val storage:FirebaseStorage,
    private val fileManager: FileManager

) {


    suspend fun uploadImageProfileToStorage(pathUidUser: String, mediaUri: Uri): UploadResult<String> {
        return try {
            // change dispatcher to io
            val compressImage = fileManager.compressImage(mediaUri)
            val reference = storage.reference.child(pathUidUser).child(Constants.IMAGE_USER_PATH)
                .child(mediaUri.lastPathSegment!!.plus(fileManager.formatImage.imageFormatExt()))
                .putBytes(compressImage).await()
            successUpload(reference.storage.downloadUrl.await().toString())
        } catch (e: Exception) {
            errorUpload(e.message!!)
        }
    }
}



data class UploadResult<T>(
    val isSuccess: Boolean,
    val error: String?,
    val data: T?
)

fun Bitmap.CompressFormat.imageFormatExt(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    when (this) {
        Bitmap.CompressFormat.JPEG -> "jpeg"
        Bitmap.CompressFormat.PNG -> ".png"
        Bitmap.CompressFormat.WEBP -> ".webp"
        Bitmap.CompressFormat.WEBP_LOSSY -> ".webp"
        Bitmap.CompressFormat.WEBP_LOSSLESS -> ".webp"
    }
} else {
    // improve it later
    "jpeg"
}

fun <T> errorUpload(errorMessage: String): UploadResult<T> = UploadResult(false, errorMessage, null)
fun <T> successUpload(data: T?) = UploadResult(true, null, data)