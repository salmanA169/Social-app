package com.example.social.sa.core.requests

import android.util.Log
import androidx.core.net.toUri
import com.example.social.sa.core.database.SocialFireStoreDatabase
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.model.Posts
import com.example.social.sa.model.UserInfo
import com.example.social.sa.model_dto.ChatRoomDto
import com.example.social.sa.model_dto.PostsDto
import com.example.social.sa.model_dto.UsersDto
import com.example.social.sa.model_dto.toPost
import com.example.social.sa.model_dto.toUserInfo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FireStoreRequests @Inject constructor(
    private val socialFireStore: SocialFireStoreDatabase,
    private val auth: FirebaseAuth,
    private val socialFirebaseStorageRequest: SocialFirebaseStorageRequest,
    private val dispatcherProvider: DispatcherProvider
) {

    fun observeChats(): FireStoreResult<Flow<List<ChatRoomDto>>> {
        return try {
            val getChats = socialFireStore.observeChats(auth.uid!!)
            FireStoreResult(true, null, getChats)
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }
    }

    suspend fun getChatIdReference(chatId: String): DocumentReference {
        return socialFireStore.getChatDocumentReference(chatId)
    }

    suspend fun sendPost(
        postId: String,
        content: String,
        images: List<String>,
    ): FireStoreResult<Unit> {
        val currentUser =
            auth.currentUser ?: return FireStoreResult(false, "User not logged in", null)
        val userNameId = socialFireStore.getUserIdByUserUUID(currentUser.uid)
        return try {
            withContext(dispatcherProvider.io) {
                val uploadImages = images.map {
                    async {
                        // TODO: change to save posts reference
                        socialFirebaseStorageRequest.uploadImageProfileToStorage(
                            currentUser.uid,
                            it.toUri()
                        ).data!!
                    }
                }
                socialFireStore.sendPost(
                    PostsDto(
                        postId,
                        currentUser.uid,
                        currentUser.displayName!!, userNameId,
                        currentUser.photoUrl.toString(),
                        Timestamp.now(),
                        content, uploadImages.awaitAll(),
                    )
                )

            }
            FireStoreResult(true, null, null)
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }
    }

    fun getPostsFlow() = socialFireStore.getPosts()
    suspend fun checkUsernameAvailable(username: String): FireStoreResult<Boolean> {
        val getUsers = socialFireStore.getUsersName()
        return if (getUsers.contains(username)) {
            FireStoreResult(false, "Username already exists", null)
        } else {
            FireStoreResult(true, null, true)
        }
    }

    suspend fun getUserInfoByUUID(userUUID: String): FireStoreResult<UserInfo> {
        return try {
            val userInfo = socialFireStore.getUserInfoByUUID(userUUID)
            FireStoreResult(true, null, userInfo.toUserInfo())
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }
    }

    suspend fun saveUser(usersDto: UsersDto): FireStoreResult<Boolean> {
        return try {
            socialFireStore.saveUser(usersDto.copy(userUUID = auth!!.uid!!))
            FireStoreResult(true, null, true)
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }
    }

    suspend fun getPostsByUserUUID(userUUID: String): List<Posts> {
        return socialFireStore.getPostsByUserUUID(userUUID).map { it.toPost() }
    }

    suspend fun createChat(chatRoomDto: ChatRoomDto): FireStoreResult<Boolean> {
        return try {
            socialFireStore.createChatRoom(chatRoomDto)
            FireStoreResult(true, null, true)
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }

    }

    suspend fun getChatIfExist(myUUID: String, otherUUID: String): FireStoreResult<String?> {
        return try {
            val documentReference = socialFireStore.getChatIfExist(myUUID, otherUUID)
            FireStoreResult(true, null, documentReference)
        } catch (e: Exception) {
            Log.e("FireStoreRequest", "getChatIfExist: called error", e)
            FireStoreResult(false, e.message, null)
        }
    }

    suspend fun getCurrentChatReference(chatRoomId: String): FireStoreResult<DocumentReference> {
        return try {
            val documentReference = socialFireStore.getCurrentChatById(chatRoomId)
            FireStoreResult(true, null, documentReference)
        } catch (e: Exception) {
            FireStoreResult(false, e.message, null)
        }
    }
//    fun observeMessage(chatRoomId: String) = socialFireStore.observeMessage(chatRoomId)
}

data class FireStoreResult<T>(
    val isSuccess: Boolean,
    val error: String?,
    val data: T?
)