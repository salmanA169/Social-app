package com.example.social.sa.screens.register.info_register

import androidx.compose.runtime.Immutable

@Immutable
data class InfoRegisterState(
    val email:String = "",
    val displayName:String = "",
    val password:String = "",
    val imageUri:String? = null ,
    val isLoading:Boolean = false,
)
