package com.example.social.sa.model

import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class UserInfo(
    val userUUID:String,
    val userId:String,
    val displayName:String,
    val imageUri:String,
    val createdAt:Timestamp,
    val email:String,
    val followers:Long,
    val following:Long,
    val postsCount:Long,
    val bio:String
)
