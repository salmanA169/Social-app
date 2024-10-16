package com.example.social.sa.model_dto

import androidx.annotation.Keep
import com.example.social.sa.model.Posts
import com.example.social.sa.utils.toLocalDateTime
import com.google.firebase.Timestamp
@Keep
data class PostsDto(
    val postId: String = "",
    val senderUID: String = "",
    val senderDisplayName: String = "",
    val senderUserId:String="",
    val senderImage: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val content: String = "",
    val images: List<String> = emptyList(),
    val likes: Int = 0,
    val comments: Int = 0,
    val share: Int = 0
)
fun List<PostsDto>.toPosts() = map { it.toPost() }
fun PostsDto.toPost() = Posts(
    postId,
    senderUID,
    senderDisplayName,
    senderUserId,
    senderImage,
    timestamp.toLocalDateTime(),
    content,
    images,
    likes,
    comments,
    share
)

