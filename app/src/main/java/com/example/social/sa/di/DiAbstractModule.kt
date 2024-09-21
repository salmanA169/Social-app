package com.example.social.sa.di

import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.coroutine.DispatcherProviderImpl
import com.example.social.sa.repository.messageRepo.MessageRepository
import com.example.social.sa.repository.messageRepo.MessageRepositoryImpl
import com.example.social.sa.repository.registerRepository.RegisterRepository
import com.example.social.sa.repository.registerRepository.RegisterRepositoryImpl
import com.example.social.sa.repository.registerRepository.TestRegisterRepository
import com.example.social.sa.repository.userRepository.UserRepository
import com.example.social.sa.repository.userRepository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DiAbstractModule {

    @Binds
    @Singleton
    abstract fun bindDispatcher(
        dispatcherProviderImpl: DispatcherProviderImpl
    ):DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindRegisterRepo(
        registerRepositoryImpl:RegisterRepositoryImpl
    ):RegisterRepository

    @Binds
    @Singleton
    abstract fun bindUserInfoRepo(
        userInfoRepository:UserRepositoryImpl
    ):UserRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepo(
        messageRepositoryImpl: MessageRepositoryImpl
    ):MessageRepository
}