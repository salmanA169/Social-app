package com.example.social.sa.core.requests

import androidx.core.net.toUri
import com.example.social.sa.core.database.SocialFireStoreDatabase
import com.example.social.sa.model_dto.PostsDto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FireStoreRequests @Inject constructor(
    private val socialFireStore: SocialFireStoreDatabase,
    private val auth: FirebaseAuth,
    private val socialFirebaseStorageRequest: SocialFirebaseStorageRequest
) {
    suspend fun sendPost(
        postId: String,
        content: String,
        images: List<String>,
    ): FireStoreResult<Unit> {
        val currentUser = auth.currentUser ?: return FireStoreResult(false,"User not logged in",null)
        return  try {
            // TODO: temp solution
            val uploadImages = images.map {
                socialFirebaseStorageRequest.uploadImageProfileToStorage(currentUser.uid,it.toUri()).data!!
            }
            socialFireStore.sendPost(
                PostsDto(
                    postId,
                    currentUser.uid,
                    currentUser.displayName!!,
                    currentUser.photoUrl.toString(),
                    Timestamp.now(),
                    content, uploadImages
                )
            )
            FireStoreResult(true,null,null)
        }catch (e:Exception){
            FireStoreResult(false,e.message,null)
        }
    }
    fun getPostsFlow() = socialFireStore.getPosts()
}

data class FireStoreResult<T>(
    val isSuccess: Boolean,
    val error: String?,
    val data: T?
)