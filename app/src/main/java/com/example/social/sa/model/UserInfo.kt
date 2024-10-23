package com.example.social.sa.model

import com.example.social.sa.model_dto.UsersDto
import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class UserInfo(
    val userUUID: String,
    val userId: String,
    val displayName: String,
    val imageUri: String,
    val createdAt: Timestamp,
    val email: String,
    val followers: Long,
    val following: Long,
    val postsCount: Long,
    val bio: String,
    // TODO: maybe remove it no need
    val chatsRoom: List<String>
)
fun UserInfo.doesMatchSearchQuery(query:String):Boolean {
    val listCombinations = listOf(
        "$userId$displayName",
        "$userId $displayName",
        "${userId.first()} ${displayName.first()}",
    )
    return listCombinations.any{
        it.contains(query, ignoreCase = true)
    }
}
fun UserInfo.toUserDto() = UsersDto(
    userUUID = userUUID,
    userId = userId,
    displayName = displayName,
    imageUri = imageUri,
    createdAt = createdAt,
    email, followers, following, postsCount, bio
)
