package com.example.social.sa.screens.camera

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.social.sa.Screens
import com.example.social.sa.component.CameraPreview

fun NavGraphBuilder.cameraDest(navController: NavController) {

    composable(Screens.CameraPreviewScreen.route) {
        Column {
            val context = LocalContext.current
            val cameraController = remember {
                LifecycleCameraController(context).apply {
                    setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
                }
            }
            CameraPreview(lifecycleCycleCamera =cameraController, modifier = Modifier.fillMaxSize())
        }
    }
}