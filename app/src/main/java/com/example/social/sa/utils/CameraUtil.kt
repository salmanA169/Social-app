package com.example.social.sa.utils

import androidx.camera.core.CameraSelector

fun CameraSelector.updateCurrentCamera() =
    if (this == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA