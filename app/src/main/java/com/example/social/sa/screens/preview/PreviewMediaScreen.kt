package com.example.social.sa.screens.preview

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.core.MediaType

@Composable
fun PlayVideo(
    modifier: Modifier,
    mediaItem: MediaItem
) {
    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()
    LaunchedEffect(key1 = Unit) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(factory = {
        PlayerView(context).apply {
            player = exoPlayer
        }

    }, modifier)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.mediaPreviewDest(
    navController: NavController,
) {
    composable<Screens.MediaPreviewScreen> {
        val mediaData = it.toRoute<Screens.MediaPreviewScreen>()
        PreviewMediaScreen(
            mediaScreenData = MediaScreenData(
                mediaType = MediaType.valueOf(mediaData.mediaType),
                mediaUri = mediaData.uri,
            )
        ) {
            navController.popBackStack()
        }
    }
}

data class MediaScreenData(
    val mediaType: MediaType,
    val mediaUri: String,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewMediaScreen(
    modifier: Modifier = Modifier,
    mediaScreenData: MediaScreenData,
    onCloseClick: () -> Unit,
    ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        FilledTonalIconButton(onClick = { onCloseClick() }, modifier = Modifier.offset(16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.close_icon),
                contentDescription = "Close Icon"
            )
        }
        when (mediaScreenData.mediaType) {
            MediaType.IMAGE -> {
                    AsyncImage(
                        model = mediaScreenData.mediaUri,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                    )
            }

            MediaType.VIDEO -> {
                PlayVideo(
                    modifier = Modifier.fillMaxSize(),
                    mediaItem = MediaItem.fromUri(mediaScreenData.mediaUri)
                )
            }
        }

    }
}
