package com.example.social.sa.screens.message

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.model.Message
import com.example.social.sa.model.MessageType
import com.example.social.sa.ui.theme.SocialTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

fun NavGraphBuilder.messageDest(navController: NavController) {
    composable<Screens.MessageRoute> {
        val getArg = it.toRoute<Screens.MessageRoute>()
        val messageViewModel = hiltViewModel<MessageViewModel>()
        val state by messageViewModel.state.collectAsStateWithLifecycle()
        val myUUId = rememberSaveable() {
            Firebase.auth.currentUser!!.uid
        }
        LaunchedEffect(key1 = true) {
            messageViewModel.addChatId(getArg.chatId)
        }
        MessageScreen(messageState = state, myUUID = myUUId, onEvent = messageViewModel::onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(messageState: MessageState, myUUID: String,onEvent: (MessageScreenEvent) -> Unit = {}) {
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        onEvent(MessageScreenEvent.SendMessage("",it))
    }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "Salman ")
        }, navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = ""
                    )
                }
                AsyncImage(
                    model = messageState.chatInfo?.imageUri ?: "",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                reverseLayout = true,
                contentPadding = PaddingValues(6.dp)
            ) {
                items(messageState.messages, key = {
                    it.messageId
                }) {
                    MessageItem(
                        messageItemColors = messageState.getMessagesItemColors(
                            isMessageFromMe = it.isMessageFromMe(myUUID)
                        ), message = it, myUId = myUUID
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            MessageInput(onSendClick = {onEvent(MessageScreenEvent.SendMessage(it,null))}) {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }
    }
}

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit,
    onImageClick: () -> Unit
) {
    var textValue by rememberSaveable {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { textValue = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .imePadding(), shape = RoundedCornerShape(50f),
        trailingIcon = {
            AnimatedContent(targetState = textValue.isNotEmpty(), label = "") {
                when (it) {
                    true ->
                        FilledIconButton(onClick = {
                            onSendClick(textValue)
                            textValue = ""
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.send_icon),
                                contentDescription = ""
                            )
                        }

                    false ->
                        IconButton(onClick = {
                            onImageClick()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.image_icon),
                                contentDescription = ""
                            )
                        }
                }
            }
        })
}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    messageItemColors: MessageItemColors,
    message: Message,
    myUId: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            colors = CardDefaults.cardColors(
                contentColor = messageItemColors.contentColor,
                containerColor = messageItemColors.backgroundColor
            ),
            modifier = Modifier.align(if (message.isMessageFromMe(myUId)) Alignment.End else Alignment.Start)
        ) {
            if (message.messageType == MessageType.IMAGE) {
                AsyncImage(model = message.image, contentDescription = "message image")
            }else if (message.messageType == MessageType.TEXT){
                Text(
                    text = message.content ?: "",
                    modifier = Modifier.padding(8.dp)

                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun MessageScreenPreview() {
    SocialTheme {
        MessageScreen(
            messageState = MessageState(
                messages = listOf(
                    Message("fds", "sa", "salman", messageType = MessageType.TEXT),
                    Message("fsdf", "", "salman", messageType = MessageType.TEXT),
                    Message("fdswq", "sa", "salman", messageType = MessageType.TEXT),
                    Message("fssadf", "", "salman", messageType = MessageType.TEXT),
                )
            ), myUUID = ""
        )
    }
}