package com.example.social.sa.core.database

import android.util.Log
import com.example.social.sa.model.Posts
import com.example.social.sa.model_dto.ChatRoomDto
import com.example.social.sa.model_dto.PostsDto
import com.example.social.sa.model_dto.UsersDto
import com.example.social.sa.model_dto.toPosts
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SocialFireStoreDatabase @Inject constructor(
    private val fireStore: FirebaseFirestore
) {

    suspend fun getAllUsers():List<UsersDto>{
        return fireStore.collection(Collections.USERS_COLLECTIONS).get().await().toObjects()
    }
    fun observeChats(myUUID: String): Flow<List<ChatRoomDto>> {
        val chatRoomFlow = fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS).snapshots()
        return chatRoomFlow.map {
            it.documents.map {
                it.toObject(ChatRoomDto::class.java)!!
            }.filter {
                it.participants.find { it.userUid == myUUID } != null
            }
        }
    }

    suspend fun getChatDocumentReference(chatID: String): DocumentReference {
        return fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS)
            .whereEqualTo("chatRoomId", chatID).get().await().documents.first().reference
    }

    suspend fun sendPost(postsDto: PostsDto) {
        fireStore.collection(Collections.POSTS_COLLECTIONS).add(postsDto).await()
    }

    fun getPosts(): Flow<List<Posts>> =
        fireStore.collection(Collections.POSTS_COLLECTIONS).dataObjects<PostsDto>().map {
            it.toPosts()
        }

    suspend fun getUsersName(): List<String> {
        val usersCollections = fireStore.collection(Collections.USERS_COLLECTIONS).get().await()
        return usersCollections.toObjects(UsersDto::class.java).map { it.userId }
    }

    suspend fun getUserInfoByUUID(userUUID: String): UsersDto {
        return fireStore.collection(Collections.USERS_COLLECTIONS).document(userUUID).get().await()
            .toObject(UsersDto::class.java)!!
    }

    suspend fun saveUser(usersDto: UsersDto) {
        fireStore.collection(Collections.USERS_COLLECTIONS).document(usersDto.userUUID)
            .set(usersDto).await()
    }

    suspend fun getUserIdByUserUUID(userUUID: String): String {
        return fireStore.collection(Collections.USERS_COLLECTIONS).document(userUUID).get().await()
            .toObject(UsersDto::class.java)!!.userId
    }

    suspend fun getPostsByUserUUID(userUUID: String): List<PostsDto> {
        return fireStore.collection(Collections.POSTS_COLLECTIONS)
            .whereEqualTo("senderUID", userUUID).get().await().toObjects(PostsDto::class.java)
    }

    suspend fun createChatRoom(chatRoomDto: ChatRoomDto): DocumentReference {
        return fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS).add(chatRoomDto).await()
    }

    suspend fun getChatIfExist(myUUID: String, otherUUID: String): String? {
        val getChats = fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS).get().await()
            .toObjects<ChatRoomDto>()
        val filterChats = getChats.filter {
            it.participants.find { it.userUid == myUUID } != null && it.participants.find { it.userUid == otherUUID } != null
        }.firstOrNull()
        Log.d("SocialFireStoreCheckChat", "getChatIfExist: chats $getChats")
        Log.d("SocialFireStoreCheckChat", "is Exist $filterChats")
        return filterChats?.chatRoomId
    }

    suspend fun getCurrentChatById(chatId: String): DocumentReference {
        return fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS)
            .whereEqualTo("chatRoomId", chatId).get().await().documents[0].reference
    }

//    fun observeMessage(chatId: String): Flow<List<MessageDto>> {
//        return fireStore.collection(Collections.CHAT_ROOM_COLLECTIONS)
//            .whereArrayContains("chatRoomId", chatId).dataObjects<MessageDto>()
//    }
}
