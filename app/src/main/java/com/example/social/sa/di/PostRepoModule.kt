package com.example.social.sa.di

import com.example.social.sa.repository.postRepository.PostRepository
import com.example.social.sa.repository.postRepository.PostsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class PostRepoModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPostRepo(postsRepositoryImpl: PostsRepositoryImpl): PostRepository

}