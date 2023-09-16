package com.example.social.sa.di

import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.coroutine.DispatcherProviderImpl
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
}