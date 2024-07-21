package com.example.social.sa.component

import androidx.camera.core.AspectRatio.RATIO_16_9
import androidx.camera.core.ImageCapture
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionFilter
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.takePicture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import coil.size.Size
import java.util.concurrent.Executors


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleCycleCamera: LifecycleCameraController
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply {
            controller = lifecycleCycleCamera
            lifecycleCycleCamera.imageCaptureResolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()
            lifecycleCycleCamera.bindToLifecycle(lifeCycleOwner)
        }
    }, modifier = modifier)
}