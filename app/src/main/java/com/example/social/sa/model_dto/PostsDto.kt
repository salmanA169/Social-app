package com.example.social.sa.model_dto

import com.example.social.sa.model.CommentsPost
import com.example.social.sa.model.Posts
import com.example.social.sa.utils.format
import com.google.firebase.Timestamp

data class PostsDto(
    val postId:String = "",
    val uidUser:String = "",
    val profileUser:String = "",
    val userName:String= "",
    val dateTime:Timestamp = Timestamp.now(),
    val imageContent:String? = null,
    val contentText:String? = null,
    val comments:List<CommentsPost> = emptyList(),
    val likes:List<String> = emptyList()
)
fun PostsDto.toPost() = Posts(
    postId,uidUser,profileUser,userName,dateTime.format(),imageContent,contentText?:"",comments,likes
)

