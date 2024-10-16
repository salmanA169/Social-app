package com.example.social.sa.repository.messageRepo

import android.net.Uri
import android.util.Log
import com.example.social.sa.core.database.Collections
import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.core.requests.SocialFirebaseStorageRequest
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.model.Message
import com.example.social.sa.model.MessageType
import com.example.social.sa.model_dto.ChatRoomDto
import com.example.social.sa.model_dto.ChatRoomUserInfo
import com.example.social.sa.model_dto.MessageDto
import com.example.social.sa.model_dto.MessageStatus
import com.example.social.sa.model_dto.toMessage
import com.example.social.sa.utils.generateID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val fireStoreRequests: FireStoreRequests,
    private val firebaseAuth: FirebaseAuth,
    private val firestoreStorageRequest: SocialFirebaseStorageRequest
) : MessageRepository {

    private lateinit var currentChatDocument: DocumentReference

    override suspend fun addChatId(chatId: String) {
        currentChatDocument = fireStoreRequests.getCurrentChatReference(chatId).data!!
    }


    override suspend fun createNewChat(otherUserId: String): String {
        val generateId = generateID()
        fireStoreRequests.createChat(
            ChatRoomDto(
                participants = listOf(
                    ChatRoomUserInfo(firebaseAuth.currentUser!!.uid, 0),
                    ChatRoomUserInfo(otherUserId, 0)
                ),
                lastMessage = null,
                lastMessageSender = null,
                chatRoomId = generateId
            )
        )
        return generateId
    }

    override fun observeMessages(): Flow<List<Message>> {
        return currentChatDocument.collection(Collections.MESSAGES)
            .orderBy("timestamp", Query.Direction.DESCENDING).snapshots().onEach {
                val documents = it.documents.filter {
                    val obj = it.toObject(MessageDto::class.java)
                    obj!!.status != MessageStatus.SEEN && obj.senderUID != firebaseAuth.currentUser!!.uid
                }.map { it.reference }
                readMessages(documents)
            }.onStart {
                updateReadChat()
            }.onCompletion {
                updateReadChat()
            }.map {
                it.toObjects(MessageDto::class.java).map { it.toMessage() }
            }
    }

    private suspend fun readMessages(documentsMessage: List<DocumentReference>) {
        withContext(Dispatchers.IO) {
            documentsMessage.forEach {
                launch {
                    it.update("status", MessageStatus.SEEN).await()
                }
            }
        }
    }

    override suspend fun getChatIfExist(otherUserId: String): String? {
        return fireStoreRequests.getChatIfExist(firebaseAuth.currentUser!!.uid, otherUserId).data
    }

    // TODO: improve it when sent image show loading
    override suspend fun addMessage(text: String, mediaUri: Uri?) {
        var tempMedia: String
        if (mediaUri != null) {
            tempMedia = firestoreStorageRequest.uploadMediaUser(
                firebaseAuth.currentUser!!.uid,
                mediaUri
            ).data!!
        } else {
            tempMedia = ""
        }
        val message = MessageDto(
            messageId = generateID(),
            senderUID = firebaseAuth.currentUser!!.uid,
            content = text,
            image = tempMedia,
            messageType = if (mediaUri != null) MessageType.IMAGE else MessageType.TEXT,
        )
        currentChatDocument.collection(Collections.MESSAGES).add(message).await()
        updateCurrentChat(text, firebaseAuth.currentUser!!.uid)
    }

    private suspend fun updateCurrentChat(lastMessage: String, lastMessageSender: String) {
        val currentChatData = currentChatDocument.get().await().toObject<ChatRoomDto>()!!
        val tempParticipants = currentChatData.participants.toMutableList()
        val findUserIndex = tempParticipants.indexOfFirst { it.userUid != lastMessageSender }
        val getUserInfo = tempParticipants.get(findUserIndex)
        val getUnreadMessage = currentChatDocument.collection(Collections.MESSAGES).get().await()
            .toObjects<MessageDto>().filter {
                it.senderUID == lastMessageSender && it.status != MessageStatus.SEEN
            }.size
        tempParticipants.set(
            findUserIndex, getUserInfo.copy(
                unreadMessageCount = getUnreadMessage
            )
        )
        currentChatDocument.set(
            currentChatData.copy(
                lastMessage = lastMessage,
                lastMessageSender = lastMessageSender,
                participants = tempParticipants
            )
        ).await()
    }

    suspend fun updateReadChat(){
        val currentChatData = currentChatDocument.get().await().toObject<ChatRoomDto>()!!
        val tempParticipants = currentChatData.participants.toMutableList()
        val findUserIndex = tempParticipants.indexOfFirst { it.userUid == firebaseAuth.currentUser!!.uid }
        val getUserInfo = tempParticipants.get(findUserIndex)

        tempParticipants.set(
            findUserIndex, getUserInfo.copy(
                unreadMessageCount = 0
            )
        )
        currentChatDocument.set(
            currentChatData.copy(
                participants = tempParticipants
            )
        ).await()
    }
    // TODO: improve it to observe live data
    override suspend fun getChatInfo(): ChatInfoState {
        val getChatInfo = currentChatDocument.get().await().toObject(ChatRoomDto::class.java)!!
        val filterUser =
            getChatInfo.participants.first { it.userUid != firebaseAuth.currentUser!!.uid }
        val getUserInfo = fireStoreRequests.getUserInfoByUUID(filterUser.userUid).data!!
        return ChatInfoState(
            displayName = getUserInfo.displayName,
            isLastMessageFromMe = getChatInfo.lastMessageSender!! == firebaseAuth.currentUser!!.uid,
            imageUri = getUserInfo.imageUri,
            lastMessage = getChatInfo.lastMessage!!,
            chatId = getChatInfo.chatRoomId!!,
            unReadMessages = 0
        )
    }
}