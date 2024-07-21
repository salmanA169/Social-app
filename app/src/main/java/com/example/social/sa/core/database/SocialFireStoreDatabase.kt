package com.example.social.sa.core.database

import com.example.social.sa.model.Posts
import com.example.social.sa.model_dto.PostsDto
import com.example.social.sa.model_dto.toPosts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SocialFireStoreDatabase @Inject constructor(
    private val fireStore: FirebaseFirestore
) {
    suspend fun sendPost(postsDto: PostsDto) {
        fireStore.collection(Collections.POSTS_COLLECTIONS).add(postsDto).await()
    }

    fun getPosts(): Flow<List<Posts>> =
        fireStore.collection(Collections.POSTS_COLLECTIONS).dataObjects<PostsDto>().map {
            it.toPosts()
        }
}
