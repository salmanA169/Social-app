package com.example.social.sa.core.requests

import com.example.social.sa.core.database.Collections
import com.example.social.sa.core.database.SocialFireStoreDatabase
import com.example.social.sa.model_dto.FollowDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FollowRequest @Inject constructor(
    private val socialFireStoreDatabase: FirebaseFirestore,
    private val auth:FirebaseAuth
) {

    suspend fun addFollowRequest(userUid:String):FollowRequestResult<Boolean>{
        return try {
            socialFireStoreDatabase.collection(Collections.FOLLOW).document().set(FollowDto(userUid,auth.currentUser!!.uid)).await()
            FollowRequestResult(true,true)
        }catch (e:Exception){
            FollowRequestResult(false,false,e.message.toString())
        }
    }
}
data class FollowRequestResult<T>(
    val data:T,
    val isSuccess:Boolean,
    val errorMessage:String?= null
)
data class FollowResult(
    val followers:Long,
    val following:Long,
    val postCount:Long
)