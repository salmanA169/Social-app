package com.example.social.sa.repository.postRepository

import com.example.social.sa.core.database.SocialFireStoreDatabase
import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.core.requests.FireStoreResult
import com.example.social.sa.model.Posts
import com.example.social.sa.utils.generateID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PostsRepositoryImpl @Inject constructor(
    private val socialFireStoreDatabase: FireStoreRequests
) :PostRepository{
    override suspend fun sendPost(content: String, images: List<String>): FireStoreResult<Unit> {
        val postID = generateID()
        return socialFireStoreDatabase.sendPost(postID,content, images)
    }

    override fun getPostsFlow(): Flow<List<Posts>> {
        return socialFireStoreDatabase.getPostsFlow()
    }

    override suspend fun getPostsByUserUUID(userUUID: String): List<Posts> {
        return socialFireStoreDatabase.getPostsByUserUUID(userUUID)
    }
}