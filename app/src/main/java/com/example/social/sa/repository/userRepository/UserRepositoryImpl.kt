package com.example.social.sa.repository.userRepository

import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.core.requests.FollowRequest
import com.example.social.sa.model.UserInfo
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseStoreRequests: FireStoreRequests,
    private val followRequest: FollowRequest
):UserRepository {
    override suspend fun getUserInfoByUUID(userUUID: String): UserInfo {
        val userInfoResult = firebaseStoreRequests.getUserInfoByUUID(userUUID)
        if (userInfoResult.isSuccess){
            return userInfoResult.data!!
        }else{
            throw Exception(userInfoResult.error)
        }
    }

    override suspend fun requestFollow(userUid: String) {
        followRequest.addFollowRequest(userUid).apply {
            // TODO: set later
        }
    }
}