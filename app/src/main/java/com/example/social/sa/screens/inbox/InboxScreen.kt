package com.example.social.sa.screens.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.social.sa.Screens
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.repository.inboxRepo.InboxRepository
import com.example.social.sa.ui.theme.SocialTheme

fun NavGraphBuilder.inboxDest(navController: NavController,paddingValues: PaddingValues) {
    composable(Screens.InboxScreen.route) {
        val inboxViewModel = hiltViewModel<InboxViewModel>()
        val inboxState by inboxViewModel.inboxState.collectAsStateWithLifecycle()
        LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
            inboxViewModel.stopObserve()
        }
        InboxScreen(inboxState = inboxState,paddingValues)
    }
}

@Composable
fun InboxScreen(inboxState: InboxState,paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues),
        contentPadding = PaddingValues(6.dp)
    ) {
        // TODO: add key and improve performance and ui
        items(inboxState.chats){
            Row {
                AsyncImage(
                    model = it.imageUri,
                    contentDescription = "image user",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Column {
                    Text(text = it.displayName)
                    Text(text = it.lastMessage)

                }
            }
        }
    }
}

@Preview
@Composable
private fun InboxPreview() {
    SocialTheme {

    }
}