package com.example.social.sa.repository.registerRepository

import android.content.Intent
import android.content.IntentSender
import com.example.social.sa.model.SignInResult
import com.example.social.sa.model.UsernameResult

interface RegisterRepository {

    suspend fun signOut():Boolean
    suspend fun signInEmailAndPassword(email:String,password:String):SignInResult
    suspend fun getGoogleAuthResult():IntentSender?
    suspend fun signUpEmailAndPassword(email: String,password: String,userName:String,imageUri:String):SignInResult
    suspend fun signInGoogle(intent:Intent):SignInResult
    suspend fun checkUserNameAvailable(userName: String):UsernameResult
    suspend fun saveUser(email: String, userName: String, imageUri: String, displayName:String)
    suspend fun updateUserProfile(imageUri: String)
}