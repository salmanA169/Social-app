package com.example.social.sa.repository.userRepository

import com.example.social.sa.core.requests.FollowResult
import com.example.social.sa.model.UserInfo

interface UserRepository {
    suspend fun getUserInfoByUUID(userUUID:String):UserInfo
    suspend fun requestFollow(userUid:String)
    suspend fun getFollowUser(userUid:String):FollowResult
    suspend fun unFollowRequest(userUid:String)
}