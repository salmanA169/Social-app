package com.example.social.sa.repository.userRepository

import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.core.requests.FollowRequest
import com.example.social.sa.core.requests.FollowResult
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

    override suspend fun getFollowUser(userUid: String): FollowResult {
        return followRequest.getFollowUser(userUid).data
    }

    override suspend fun unFollowRequest(userUid: String) {
        followRequest.unFollowRequest(userUid)
    }

    override suspend fun requestFollow(userUid: String) {
        followRequest.addFollowRequest(userUid).apply {
            // TODO: set later
        }
    }
}