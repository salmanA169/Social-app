package com.example.social.sa.screens.home.add_edit_post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import com.example.social.sa.Constants
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.core.MediaType
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark
import com.example.social.sa.utils.formatSecondAndMinute
import java.time.LocalDate
import java.time.LocalTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

fun NavGraphBuilder.addEditPostDest(navController: NavController) {
    composable(Screens.PostReviewScreen.route, enterTransition = {
        slideInVertically()
    }) { navBackEntry ->

        val viewModel = hiltViewModel<PostEditPostViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val effect by viewModel.effect.collectAsStateWithLifecycle()
        val resultCameraImage by
        navBackEntry.savedStateHandle.getStateFlow<String?>(Constants.BITMAP_RESULT_KEY, null)
            .collectAsStateWithLifecycle()

        LaunchedEffect(key1 = resultCameraImage) {
            resultCameraImage?.let {
                viewModel.onEvent(AddEditPostEvent.PickedFromCamera(MediaType.Image(0,it,0)))
            }
        }
        LaunchedEffect(key1 = effect) {
            when (effect) {
                is AddEditPostEffect.Navigate -> {
                    navController.navigate((effect as AddEditPostEffect.Navigate).route)
                }

                null -> {

                }
            }
            viewModel.resetEffect()
        }
        AddEditPostScreen(state, viewModel::onEvent)
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditPostScreen(
    state: AddEditPostState,
    onEvent: (AddEditPostEvent) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        uris?.let {
            it.forEach {
                onEvent(AddEditPostEvent.PickImage(it.toString()))
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.create_post))
                }, navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = ""
                        )
                    }
                }, actions = {
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.send))
                    }
                }
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                cursorBrush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary
                    )
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textDirection = TextDirection.Content
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.what_happening),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    it()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .horizontalScroll(
                        rememberScrollState()
                    ),
            ) {
                state.pickedImage.forEach {
                    PickedImage(mediaType = it) {
                        onEvent(AddEditPostEvent.DeleteImage(it))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = state.pickedImage.isEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier, contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        CameraIcon()
                    }
                    items(state.images) {
                        PickImages(mediaType = it, onEvent = onEvent)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            IconsLayout(
                onEvent = onEvent,
                onImagePickerClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                })
        }
    }
}

@Composable
fun IconsLayout(
    modifier: Modifier = Modifier,
    onEvent: (AddEditPostEvent) -> Unit,
    onImagePickerClick: () -> Unit
) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .padding(8.dp)
            .imePadding()
    ) {
        IconButton(onClick = { onImagePickerClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.image_icon),
                contentDescription = "image",
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { onEvent(AddEditPostEvent.Navigate(Screens.CameraPreviewScreen.route)) }) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "camera",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PickedImage(
    modifier: Modifier = Modifier,
    mediaType: MediaType,
    onImageDelete: (MediaType) -> Unit
) {
    val context = LocalContext.current
    val videoEnabledLoader = ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }.build()
    Box {
        AsyncImage(
            imageLoader = videoEnabledLoader,
            model = mediaType.uri,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp, 250.dp)
                .clip(RoundedCornerShape(25f))
        )
        FilledTonalIconButton(
            onClick = { onImageDelete(mediaType) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(painter = painterResource(id = R.drawable.close_icon), contentDescription = "")
        }
        if (mediaType is MediaType.Video) {
            Icon(
                painter = painterResource(id = R.drawable.play_preview_video_icon),
                contentDescription = "play preview video",
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}

@Composable
fun CameraIcon() {
    OutlinedButton(modifier = Modifier
        .size(100.dp), shape = RoundedCornerShape(25f), onClick = { /*TODO*/ }) {
        Icon(painter = painterResource(id = R.drawable.camera_icon), contentDescription = "")
    }
}

@PreviewBothLightAndDark
@Composable
private fun AddEditPreview() {
    SocialTheme {
        AddEditPostScreen(AddEditPostState())
    }
}

@Preview
@Composable
private fun PikedImagePreview() {
    SocialTheme {
        PickImages(
            mediaType = MediaType.Video(
                0,LocalDate.now().toEpochDay().toInt(),
                "",
                20000L
            )
        ) {

        }
    }
}

@Composable
fun PickImages(
    modifier: Modifier = Modifier,
    mediaType: MediaType,
    onEvent: (AddEditPostEvent) -> Unit
) {
    val context = LocalContext.current
    val videoEnabledLoader = ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }.build()
    val isVideo = remember(mediaType) {
        mediaType is MediaType.Video
    }
    Box {
        AsyncImage(
            imageLoader = videoEnabledLoader,
            model = mediaType.uri,
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(25f))
                .clickable {
                    onEvent(AddEditPostEvent.PickImage(mediaType.uri))
                },
            contentScale = ContentScale.Crop
        )
        if (isVideo) {
            Text(
                text = (mediaType as MediaType.Video).duration.milliseconds.formatSecondAndMinute(),
                modifier = Modifier.offset(6.dp, (-8).dp).background(MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(16.dp)
                ).padding(10.dp, 4.dp).align(Alignment.BottomStart),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

}