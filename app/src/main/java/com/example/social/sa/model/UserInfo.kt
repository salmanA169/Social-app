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
    val bio: String
)

fun UserInfo.toUserDto() = UsersDto(
    userUUID = userUUID,
    userId = userId,
    displayName = displayName,
    imageUri = imageUri,
    createdAt = createdAt,
    email, followers, following, postsCount, bio
)
