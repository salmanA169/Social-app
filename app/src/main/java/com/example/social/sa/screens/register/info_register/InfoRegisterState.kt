package com.example.social.sa.screens.register.info_register

import androidx.compose.runtime.Immutable
import com.example.social.sa.screens.register.InputData

@Immutable
data class InfoRegisterState(
    val email:InputData = InputData(),
    val displayName:InputData = InputData(),
    val userName:InputData = InputData(),
    val imageUri:String? = null ,
    val isLoading:Boolean = false,
)
