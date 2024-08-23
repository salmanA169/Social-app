package com.example.social.sa.screens.register

import android.content.IntentSender
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.social.sa.R
import com.example.social.sa.model.AuthProvider

@Immutable
data class RegisterState(
    val email:InputData = InputData(),
    val password:InputData = InputData(),
    val registerType:RegisterType = RegisterType.SIGN_IN,
    val googleIntentSender: IntentSender? = null
)

@Immutable
data class InputData(
    val content:String = "",
    var isError :Boolean = false,
    var errorText:String = ""
){
    fun setError(message:String){
        isError = true
        errorText = message
    }
    fun clearError(){
        isError = false
        errorText = ""
    }
}
enum class RegisterType{
    SIGN_IN,SIGN_UP;

    @Composable
    fun localizeString():String {
        return when(this){
            SIGN_IN -> stringResource(id = R.string.sign_in)
            SIGN_UP -> stringResource(id = R.string.sign_up)
        }
    }
}
