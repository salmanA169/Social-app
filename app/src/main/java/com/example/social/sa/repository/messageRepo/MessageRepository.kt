package com.example.social.sa.repository.messageRepo

import android.net.Uri
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.model.Message
import com.example.social.sa.model_dto.ChatRoomDto
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages():Flow<List<Message>>
    suspend fun addMessage(text:String,mediaUri: Uri?)
    suspend fun getChatInfo():ChatInfoState
    suspend fun addChatId(chatId: String)
    suspend fun createNewChat(otherUserId: String):String
    suspend fun getChatIfExist(otherUserId:String):String?
}