package com.example.social.sa.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalImageCaptureOutputFormat
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OUTPUT_FORMAT_JPEG_ULTRA_HDR
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.camera.core.internal.utils.ImageUtil
import androidx.camera.core.takePicture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

fun CameraSelector.updateCurrentCamera() =
    if (this == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

@OptIn(ExperimentalImageCaptureOutputFormat::class)
fun LifecycleCameraController.takePhoto(
    onPhotoTake:(Bitmap)->Unit,
    context: Context
){

    takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTake(rotatedBitmap(image))
            }
        }
    )
}

private fun rotatedBitmap(imageProxy: ImageProxy):Bitmap{
    val matrix = Matrix().apply {
        postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
    }
    val rotatedBitmap = Bitmap.createBitmap(
        imageProxy.toBitmap(),
        0,0,
        imageProxy.width,
        imageProxy.height,
        matrix,
        true
    )
    return rotatedBitmap
}