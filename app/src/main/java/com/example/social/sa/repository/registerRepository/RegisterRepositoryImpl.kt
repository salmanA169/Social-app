package com.example.social.sa.repository.registerRepository

import android.content.Intent
import android.content.IntentSender
import com.example.social.sa.core.requests.AuthRequest
import com.example.social.sa.model.SignInResult
import com.example.social.sa.model.UserInfoRegister
import javax.inject.Inject
class TestRegisterRepository @Inject constructor ():RegisterRepository{
    override suspend fun signOut(): Boolean {
        return true
    }

    override suspend fun signInEmailAndPassword(email: String, password: String): SignInResult {
        return SignInResult(true,null , UserInfoRegister(
            email,"test","test","",false
        ))
    }

    override suspend fun getGoogleAuthResult(): IntentSender? {
        return null
    }

    override suspend fun signUpEmailAndPassword(
        email: String,
        password: String,
        userName: String,
        imageUri: String
    ): SignInResult {
       return  SignInResult(true,null , UserInfoRegister(
            email,"test","test","",false
        ))
    }

    override suspend fun signInGoogle(intent: Intent): SignInResult {
        return  SignInResult(true,null , UserInfoRegister(
            "test","test","test","",false
        ))
    }
}

class RegisterRepositoryImpl @Inject constructor(
    private val authRequest: AuthRequest
):RegisterRepository {


    override suspend fun signOut(): Boolean {
        authRequest.signOut()
        return true
    }

    override suspend fun signInEmailAndPassword(email: String, password: String): SignInResult {
        return authRequest.signInEmail(email, password)
    }

    override suspend fun getGoogleAuthResult(): IntentSender? {
        return authRequest.signInGoogle()
    }

    override suspend fun signInGoogle(intent: Intent): SignInResult {
        return authRequest.signInGoogleResult(intent)
    }

    override suspend fun signUpEmailAndPassword(
        email: String,
        password: String,
        userName: String,
        imageUri: String
    ) :SignInResult{
        return authRequest.signUpNewUser(email, password, userName, imageUri)
    }
}