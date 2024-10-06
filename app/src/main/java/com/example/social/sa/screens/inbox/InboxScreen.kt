package com.example.social.sa.screens.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.repository.inboxRepo.InboxRepository
import com.example.social.sa.ui.theme.SocialTheme

fun NavGraphBuilder.inboxDest(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.InboxScreen.route) {
        val inboxViewModel = hiltViewModel<InboxViewModel>()
        val inboxState by inboxViewModel.inboxState.collectAsStateWithLifecycle()
        val effect by inboxViewModel.effect.collectAsStateWithLifecycle()
        LifecycleEventEffect(event = Lifecycle.Event.ON_PAUSE) {
            inboxViewModel.stopObserve()
        }
        LaunchedEffect(key1 = effect) {
            when(effect){
                is InboxEffect.NavigateToChat -> {
                    navController.navigate(Screens.MessageRoute((effect as InboxEffect.NavigateToChat).chatId))
                }
                else->Unit
            }
            inboxViewModel.resetEffect()
        }
        InboxScreen(inboxState = inboxState, paddingValues,inboxViewModel::onEvent)
    }
}

@Composable
fun InboxScreen(inboxState: InboxState, paddingValues: PaddingValues,onEvent: (InboxEvent) -> Unit={}) {
    LazyColumn(
        modifier = Modifier
            // TODO: fix ripple clickable {  } 
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues),
        contentPadding = PaddingValues(6.dp)
    ) {
        // TODO: add key and improve performance and ui
        items(inboxState.chats, key = {
            it.chatId
        }, contentType = {
            it.hashCode()
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(InboxEvent.NavigateToChat(it.chatId))
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.imageUri,
                    contentDescription = "image user",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Column {
                    Text(
                        text = it.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = it.lastMessage,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                ) {
                    Text(text = "1", modifier = Modifier.align(Alignment.Center))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@PreviewLightDark
@Composable
private fun InboxPreview() {
    SocialTheme {
        InboxScreen(
            inboxState = InboxState(
                listOf(
                    ChatInfoState("", "salmadsn", "", "dddddddddd", false),
                    ChatInfoState("salmands", "ddddddd", "", "dddd", false),
                    ChatInfoState("salman", "ssssssssss", "", "ddd", false),
                )
            ), paddingValues = PaddingValues()
        )
    }
}