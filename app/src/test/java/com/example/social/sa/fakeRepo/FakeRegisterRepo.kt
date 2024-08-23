package com.example.social.sa.fakeRepo

import android.content.Intent
import android.content.IntentSender
import com.example.social.sa.model.SignInResult
import com.example.social.sa.model.UsernameResult
import com.example.social.sa.repository.registerRepository.RegisterRepository

class FakeRegisterRepo:RegisterRepository {
    override suspend fun signOut(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signInEmailAndPassword(email: String, password: String): SignInResult {
        TODO("Not yet implemented")
    }

    override suspend fun getGoogleAuthResult(): IntentSender? {
        TODO("Not yet implemented")
    }

    override suspend fun signUpEmailAndPassword(
        email: String,
        password: String,
        userName: String,
        imageUri: String
    ): SignInResult {
        TODO("Not yet implemented")
    }

    override suspend fun signInGoogle(intent: Intent): SignInResult {
        TODO("Not yet implemented")
    }

    override suspend fun checkUserNameAvailable(userName: String): UsernameResult {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(
        email: String,
        userName: String,
        imageUri: String,
        displayName: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(imageUri: String) {
        TODO("Not yet implemented")
    }
}