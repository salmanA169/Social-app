package com.example.social.sa.repository.registerRepository

import android.content.Intent
import android.content.IntentSender
import com.example.social.sa.model.SignInResult

interface RegisterRepository {

    suspend fun signOut():Boolean
    suspend fun signInEmailAndPassword(email:String,password:String):SignInResult
    suspend fun getGoogleAuthResult():IntentSender?
    suspend fun signUpEmailAndPassword(email: String,password: String,userName:String,imageUri:String):SignInResult
    suspend fun signInGoogle(intent:Intent):SignInResult
}