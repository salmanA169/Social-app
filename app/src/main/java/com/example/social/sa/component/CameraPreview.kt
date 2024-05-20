package com.example.social.sa.component

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleCycleCamera:LifecycleCameraController
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply {
            controller = lifecycleCycleCamera
            lifecycleCycleCamera.bindToLifecycle(lifeCycleOwner)
        }
    },modifier=modifier)
}