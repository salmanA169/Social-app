package com.example.social.sa.model_dto

import com.google.firebase.Timestamp

data class FollowDto(
    val followers:String = "",
    val following:String = "",
    val date:Timestamp= Timestamp.now()
)
