package com.example.social.sa.repository.postRepository

import com.example.social.sa.core.requests.FireStoreResult
import com.example.social.sa.model.Posts
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun sendPost(
        content: String,
        images: List<String>,
    ):FireStoreResult<Unit>
    fun getPostsFlow(): Flow<List<Posts>>

    suspend fun getPostsByUserUUID(userUUID:String):List<Posts>
}