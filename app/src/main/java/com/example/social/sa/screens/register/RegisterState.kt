package com.example.social.sa.screens.register

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
)

data class InputData(
    val content:String = "",
    val isError :Boolean = false,
    val errorText:String = ""
)
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
