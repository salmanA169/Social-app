package com.example.social.sa.repository.inboxRepo

import android.util.Log
import com.example.social.sa.core.database.Collections
import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.model_dto.MessageDto
import com.example.social.sa.model_dto.MessageStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

class InboxRepositoryImpl @Inject constructor(
    private val socialFireStoreRequests: FireStoreRequests,
    private val auth: FirebaseAuth
) : InboxRepository {
    // TODO: fix unread message count when open message not update until i write message
    override fun observeChats(): Flow<List<ChatInfoState>> {
        val result = socialFireStoreRequests.observeChats()
        if (result.isSuccess) {
            return result.data!!.map { chats ->
                chats.mapNotNull {
                    if (it.lastMessageSender == null) {
                        return@mapNotNull null
                    }
                    val filterUser =
                        it.participants.firstOrNull { users -> users.userUid != auth.currentUser!!.uid }!!
                    val getUnreadMessage = it.participants.firstOrNull { it.userUid == auth.currentUser!!.uid }!!.unreadMessageCount
                    val getUserInfo =
                        socialFireStoreRequests.getUserInfoByUUID(filterUser.userUid).data!!
                    ChatInfoState(
                        displayName = getUserInfo.displayName,
                        imageUri = getUserInfo.imageUri,
                        lastMessage = it.lastMessage ?: "",
                        isLastMessageFromMe = it.lastMessageSender == auth.currentUser!!.uid,
                        chatId = it.chatRoomId!!,
                        unReadMessages = getUnreadMessage
                    )
                }
            }
        } else {
            throw Exception("Can not observe chats")
        }
    }
}