package com.example.social.sa.core.requests

import android.util.Log
import com.example.social.sa.core.database.Collections
import com.example.social.sa.core.database.SocialFireStoreDatabase
import com.example.social.sa.model_dto.FollowDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FollowRequest @Inject constructor(
    private val socialFireStoreDatabase: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun addFollowRequest(userUid: String): FollowRequestResult<Boolean> {
        return try {
            socialFireStoreDatabase.collection(Collections.FOLLOW).document()
                .set(FollowDto(userUid, auth.currentUser!!.uid)).await()
            FollowRequestResult(true, true)
        } catch (e: Exception) {
            FollowRequestResult(false, false, e.message.toString())
        }
    }

    suspend fun getFollowUser(userUid: String): FollowRequestResult<FollowResult> {
        return try {
            val postsCount = socialFireStoreDatabase.collection(Collections.POSTS_COLLECTIONS)
                .whereEqualTo("senderUID", userUid).get().await().size()
            val follow = socialFireStoreDatabase.collection(Collections.FOLLOW).get().await()
                .toObjects<FollowDto>()
            val followers = follow.filter { it.followers == userUid }.size
            val following = follow.filter { it.following == userUid }.size
            val isFollowing = follow.any {
                it.following == auth.currentUser!!.uid && it.followers == userUid
            }
            FollowRequestResult(
                FollowResult(
                    followers.toLong(),
                    following.toLong(),
                    postsCount.toLong(),
                    isFollowing
                ), true, null
            )
        } catch (e: Exception) {
            FollowRequestResult(FollowResult(0, 0, 0, false), false, e.message.toString())
        }
    }

    suspend fun unFollowRequest(userUid: String): FollowRequestResult<Boolean> {
        return try {
            socialFireStoreDatabase.collection(Collections.FOLLOW)
                .whereEqualTo("followers", userUid)
                .whereEqualTo("following", auth.currentUser!!.uid).get().await().first().reference.delete()
            FollowRequestResult(true,true)
            } catch (e:Exception){
            Log.e(javaClass.simpleName, "unFollowRequest: error called",e )
                FollowRequestResult(false, false, e.message.toString())
            }
        }
    }

    data class FollowRequestResult<T>(
        val data: T,
        val isSuccess: Boolean,
        val errorMessage: String? = null
    )

    data class FollowResult(
        val followers: Long,
        val following: Long,
        val postCount: Long,
        val isFollowing: Boolean
    )