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
    private val subCommentList = mutableListOf<Comment>()

    fun addSubComment(comment: Comment){
        subCommentList.add(comment)
    }

    fun hasSubComment():Boolean{
        return subCommentList.isNotEmpty()
    }
}

