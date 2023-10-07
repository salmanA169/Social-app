package com.example.social.sa.model

data class Posts(
    val postId:String,
    val uidUser:String,
    val profileUser:String,
    val userName:String,
    val dateTime:String,
    val imageContent:String?,
    val contentText:String,
    val comments:List<CommentsPost>,
    val likes:List<String>
)
data class CommentsPost(
    val commentId:String,
)
