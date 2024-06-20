package com.example.social.sa.screens.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.social.sa.Screens

@Composable
fun PlayVideo(
    modifier: Modifier,
    mediaItem :MediaItem
){
    // TODO: continue here add type media is image or video
    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()
    LaunchedEffect(key1 = Unit ) {
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

    },modifier)
}
fun NavGraphBuilder.mediaPreviewDest(navController: NavController){
    composable<Screens.MediaPreviewScreen>{
        val mediaType = it.toRoute<Screens.MediaPreviewScreen>()
        Box(modifier = Modifier.fillMaxSize()){
            PlayVideo(modifier = Modifier.fillMaxSize(), mediaItem = MediaItem.fromUri(mediaType.mediaType))
        }
    }
}