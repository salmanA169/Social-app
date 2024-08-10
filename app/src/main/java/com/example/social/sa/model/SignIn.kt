package com.example.social.sa.model

data class SignIn(
    val email:String,
    val password:String,
    val signInProvider:AuthProvider
)
data class SignInResult(
    val isSuccess:Boolean,
    val error:String?,
    val userInfo:UserInfoRegister? = null
)

data class UserInfoRegister(
    val email: String,
    val name:String,
    val uid:String,
    val photo:String,
    val isNewUser: Boolean
)
enum class AuthProvider{
    EMAIL,GOOGLE
}
