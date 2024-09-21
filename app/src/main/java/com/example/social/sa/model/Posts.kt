package com.example.social.sa.model

import java.time.LocalDateTime

data class Posts(
    val postId: String,
    val senderUUID: String,
    val senderDisplayName: String,
    val senderUserId:String="",
    val senderImage: String,
    val timestamp: LocalDateTime,
    val content: String,
    val images: List<String>,
    val likes: Int,
    val comments: Int,
    val share: Int
)

data class CommentsPost(
    val commentId: String,
)
