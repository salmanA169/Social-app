package com.example.social.sa.model

data class Posts(
    val postId:String,
    val senderUid:String,
    val senderId:String,
    val senderDisplayName:String,
    val senderImage:String,
    val timeStamp:Long,
    val content:String,
    val imageContent:List<String>,
    val likes:Int,
    val comments:Int,
    val share:Int
)
data class CommentsPost(
    val commentId:String,
)
