package com.example.social.sa.repository.inboxRepo

import com.example.social.sa.model.ChatInfoState
import kotlinx.coroutines.flow.Flow

interface InboxRepository {
    fun observeChats():Flow<List<ChatInfoState>>
}