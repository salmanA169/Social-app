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
import com.example.social.sa.model_dto.MessageDto
import com.example.social.sa.model_dto.toMessage
import com.example.social.sa.model_dto.toMessages
import com.example.social.sa.utils.generateID
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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

    override suspend fun createNewChat(otherUserId: String) :String{
        val generateId = generateID()
        fireStoreRequests.createChat(
            ChatRoomDto(
            participants = listOf(firebaseAuth.currentUser!!.uid, otherUserId),
            lastMessage = null,
            lastMessageSender = null,
            chatRoomId = generateId
        )
        )
        return generateId
    }

    override fun observeMessages(): Flow<List<Message>> {
        return currentChatDocument.collection(Collections.MESSAGES).orderBy("timestamp", Query.Direction.DESCENDING).snapshots().map {
            it.toObjects(MessageDto::class.java).map { it.toMessage() }
        }
    }

    override suspend fun getChatIfExist(otherUserId: String):String? {
        return fireStoreRequests.getChatIfExist(firebaseAuth.currentUser!!.uid, otherUserId).data
    }

    // TODO: improve it when sent image show loading
    override suspend fun addMessage(text:String,mediaUri: Uri?) {
        var tempMedia :String
        if (mediaUri != null){
            tempMedia = firestoreStorageRequest.uploadMediaUser(firebaseAuth.currentUser!!.uid,mediaUri).data!!
        }else{
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
    }

    // TODO: improve it to observe live data
    override suspend fun getChatInfo(): ChatInfoState {
        val getChatInfo = currentChatDocument.get().await().toObject(ChatRoomDto::class.java)!!
        val filterUser = getChatInfo.participants.first { it != firebaseAuth.currentUser!!.uid }
        val getUserInfo = fireStoreRequests.getUserInfoByUUID(filterUser).data!!
        return ChatInfoState(
            displayName = getUserInfo.displayName,
            isLastMessageFromMe = getChatInfo.lastMessageSender!! == firebaseAuth.currentUser!!.uid,
            imageUri = getUserInfo.imageUri,
            lastMessage = getChatInfo.lastMessage!!
        )
    }
}