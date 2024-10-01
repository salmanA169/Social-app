package com.example.social.sa.screens.inbox

import androidx.compose.runtime.Immutable
import com.example.social.sa.model.ChatInfoState

@Immutable
data class InboxState(
    val chats:List<ChatInfoState> = emptyList()
)
