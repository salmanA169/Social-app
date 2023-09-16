package com.example.social.sa.di

import android.content.Context
import com.example.social.sa.utils.datastore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context:Context) = context.datastore

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}