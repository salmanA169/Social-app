package com.example.social.sa.screens.register.info_register

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.social.sa.Screens

fun NavGraphBuilder.infoRegisterDest(navController: NavController) {
    composable<Screens.InfoRegisterRoute> {
        val getArg = it.toRoute<Screens.InfoRegisterRoute>()
        InfoRegisterScreen(infoRegisterState = InfoRegisterState(), email = getArg.email)
    }
}

@Composable
fun InfoRegisterScreen(
    modifier: Modifier = Modifier,
    infoRegisterState: InfoRegisterState,
    email: String
) {
    var currentImage by remember {
        mutableStateOf("")
    }
    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            currentImage = it.toString()
        }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .systemBarsPadding()
    ) {
        AsyncImage(
            model = currentImage,
            contentDescription = "",
            modifier = Modifier
                .align(CenterHorizontally)
                .size(60.dp)
                .clip(
                    CircleShape
                )
                .clickable { pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            contentScale = ContentScale.Crop
        )

        Text(text = email)
    }
}