package com.example.social.sa.model_dto

import com.google.firebase.Timestamp

data class UsersDto(
    val userUUID:String = "",
    val userId:String ="",
    val displayName:String = "",
    val imageUri:String ="",
    val createdAt: Timestamp = Timestamp.now(),
    val email:String = "",
    val followers:Long = 0,
    val following:Long = 0,
    val postsCount:Long =0 ,
    val bio:String = ""
)
