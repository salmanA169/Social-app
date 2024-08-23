package com.example.social.sa.repository.registerRepository

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.social.sa.core.requests.AuthRequest
import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.model.SignInResult
import com.example.social.sa.model.UserInfo
import com.example.social.sa.model.UserInfoRegister
import com.example.social.sa.model.UsernameResult
import com.example.social.sa.model.toUserDto
import com.example.social.sa.model_dto.UsersDto
import javax.inject.Inject
class TestRegisterRepository @Inject constructor ():RegisterRepository{
    override suspend fun signOut(): Boolean {
        return true
    }

    private val users = listOf("test","salman")
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

    override suspend fun checkUserNameAvailable(userName: String): UsernameResult {
        return if (users.contains(userName)){
            UsernameResult(false,"Username is not available")
        }else{
            UsernameResult(true,null)
        }
    }
}

class RegisterRepositoryImpl @Inject constructor(
    private val authRequest: AuthRequest,
    private val fireStoreRequests: FireStoreRequests
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

    override suspend fun updateUserProfile(imageUri: String) {
        authRequest.updateImageProfile(imageUri)
    }

    override suspend fun signInGoogle(intent: Intent): SignInResult {
        return authRequest.signInGoogleResult(intent)
    }

    override suspend fun saveUser(
        email: String,
        userName: String,
        imageUri: String,
        displayName: String
    ) {
        fireStoreRequests.saveUser(
            UsersDto(
                email =email,
                userId = userName,
                imageUri = imageUri,
                displayName = displayName,
            )
        )
    }

    override suspend fun checkUserNameAvailable(userName: String): UsernameResult {
        val getResult = fireStoreRequests.checkUsernameAvailable(userName)
        return if (getResult.isSuccess){
            UsernameResult(true,null)
        }else{
            UsernameResult(false,getResult.error)
        }
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