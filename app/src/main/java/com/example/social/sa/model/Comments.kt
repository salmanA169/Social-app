package com.example.social.sa.model

import androidx.compose.runtime.Immutable

@Immutable
data class Comment(
    val commentId:String,
    val postId:String,
    val userUid:String,
    val imageUrl:String,
    val displayName:String,
    val comment:String,
    val timestamp:String,
    val likes:Int
){
    private val replies = mutableListOf<Comment>()

    fun addReply(comment: Comment){
        replies.add(comment)
    }
    fun getReplies () = replies.toList()
    fun hasReplies():Boolean{
        return replies.isNotEmpty()
    }
}

