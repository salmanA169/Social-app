package com.example.social.sa.screens.home.add_edit_post

import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.social.sa.Screens

fun NavGraphBuilder.addEditPostDest(navController: NavController) {
    composable(Screens.PostReviewScreen.route, enterTransition = {
        slideInVertically()
    }) {
        val viewModel = hiltViewModel<PostEditPostViewModel>()
        val state by viewModel.state.collectAsState()
        AddEditPostScreen(state)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditPostScreen(
    state: AddEditPostState
) {
    var text = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        BasicTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            cursorBrush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                )
            ),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground)
        ) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                it()
                Spacer(modifier = Modifier.height(24.dp))
                FlowRow(
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                    CameraIcon()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    CameraIcon()
                }
                items(state.images) {
                    PickImages(imageUri = it)
                }
            }
        }
    }
}

@Composable
fun CameraIcon() {
    OutlinedButton(modifier = Modifier
        .size(80.dp), shape = RoundedCornerShape(25f), onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}

@Composable
fun PickImages(
    imageUri: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUri,
        contentDescription = "",
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(25f)),
        contentScale = ContentScale.Crop
    )

}