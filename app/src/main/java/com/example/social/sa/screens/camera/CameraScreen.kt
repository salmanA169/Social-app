package com.example.social.sa.screens.camera

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.CameraPreview
import com.example.social.sa.utils.updateCurrentCamera

fun NavGraphBuilder.cameraDest(navController: NavController) {

    composable(Screens.CameraPreviewScreen.route) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val context = LocalContext.current
            val cameraController = remember {
                LifecycleCameraController(context).apply {
                    setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
                }
            }
            CameraPreview(
                lifecycleCycleCamera = cameraController,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(onClick = {
                cameraController.cameraSelector =
                    cameraController.cameraSelector.updateCurrentCamera()
            }, modifier = Modifier.offset(16.dp, 16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.camera_switch_icon),
                    contentDescription = "camera switch"
                )
            }
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .size(80.dp)
                    .border(
                        3.dp, Color.White,
                        CircleShape
                    )
                    .background(Color.Transparent, CircleShape)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {},
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    )
                    .align(Alignment.BottomCenter)
            ) {

            }
        }
    }
}