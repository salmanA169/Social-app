package com.example.social.sa.core.requests

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.core.net.toUri
import com.example.social.sa.model.SignInResult
import com.example.social.sa.model.UserInfoRegister
import com.example.social.sa.utils.isEmailValid
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRequest @Inject constructor(
    private val authFirebaseAuth: FirebaseAuth,
  @ApplicationContext  private val context: Context
) {

    private val signInRequestGoogle = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("621340764167-3va8b17kre8srg5vc9fhasj6jh3daltv.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
    private val oneTapClient = Identity.getSignInClient(context)

    @Inject
    lateinit var firebaseStorage: SocialFirebaseStorageRequest

    suspend fun signInEmail(email: String,password: String):SignInResult{
        return try {
            val signInResult = authFirebaseAuth.signInWithEmailAndPassword(email,password).await().user!!
            SignInResult(
                true,null,UserInfoRegister(
                    signInResult.email!!,
                    signInResult.displayName?:"",
                    signInResult.uid,
                    signInResult.photoUrl.toString(),
                    false
                )
            )
        }catch (e:Exception){
            Log.e("AuthRequest", "signInEmail: Error", e)
            SignInResult(
                false,e.message,null
            )
        }
    }

    suspend fun signUpNewUser(email:String,password:String,userName:String,imageUri:String):SignInResult{
        return try {
            if (!email.isEmailValid){
                SignInResult(false,"Email not valid",null)
            }
            val authResult = authFirebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userInfo = authResult.user!!
            val uploadResult = firebaseStorage.uploadImageProfileToStorage(userInfo.uid,imageUri.toUri())
            if (uploadResult.isSuccess){
                val updatedProfile = userProfileChangeRequest {
                    displayName = userName
                    photoUri = imageUri.toUri()
                }
                userInfo.updateProfile(updatedProfile).await()
                SignInResult(
                    true,null, UserInfoRegister(
                        userInfo.email!!,
                        userInfo.displayName!!,
                        userInfo.uid,
                        userInfo.photoUrl?.toString()?:"",
                        true
                    )
                )
            }else{
                SignInResult(false,uploadResult.error?:"Unknown Error")
            }
        }catch (e:Exception){
            SignInResult(
                false,e.message,null
            )
        }
    }


    suspend fun signInGoogle():IntentSender?{
        return try {
            oneTapClient.beginSignIn(signInRequestGoogle).await().pendingIntent.intentSender
        }catch (e:Exception){
            null
        }
    }

    suspend fun signOut(){
        oneTapClient.signOut().await()
        authFirebaseAuth.signOut()
    }
    suspend fun signInGoogleResult(intent: Intent):SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {
            val authResult = authFirebaseAuth.signInWithCredential(googleCredential).await()
            val user = authResult.user!!
            SignInResult(
                true,null ,UserInfoRegister(
                    user.email!!,
                    user.displayName!!,
                    user.uid,
                    user.photoUrl.toString()?:"",
                    authResult.additionalUserInfo!!.isNewUser
                )
            )
        }catch (e:Exception){
            SignInResult(
                false,e.message,null
            )
        }
    }
}