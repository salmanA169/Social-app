package com.example.social.sa.screens.camera

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.social.sa.Constants
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.CameraPreview
import com.example.social.sa.screens.home.add_edit_post.PostEditPostViewModel
import com.example.social.sa.utils.saveExternalStorage
import com.example.social.sa.utils.saveTempFile
import com.example.social.sa.utils.takePhoto
import com.example.social.sa.utils.updateCurrentCamera

fun NavGraphBuilder.cameraDest(navController: NavController) {

    composable(Screens.CameraPreviewScreen.route) {navBackEntry->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding().navigationBarsPadding()
        ) {
            var currentBitmap by remember {
                mutableStateOf<Bitmap?>(null)
            }
            val context = LocalContext.current
            val cameraController = remember {
                LifecycleCameraController(context).apply {
                    setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
                }
            }
            if (currentBitmap != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(currentBitmap).build(),
                    contentDescription = "camera preview",
                    modifier = Modifier.fillMaxSize(),
                )
                Button(onClick = {
                 navController.previousBackStackEntry?.savedStateHandle?.set(Constants.BITMAP_RESULT_KEY,
                     saveTempFile("sssssssssss",currentBitmap!!)
                 )
                    navController.popBackStack()
                }, modifier = Modifier.align(Alignment.BottomEnd).offset(-16.dp,-16.dp)) {
                    Text(text = stringResource(id = R.string.use_photo))
                }
            } else {
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
                            onClick = {
                                cameraController.takePhoto({
                                    currentBitmap = it
                                }, context)
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple()
                        )
                        .align(Alignment.BottomCenter)
                ) {

                }
            }

        }
    }
}