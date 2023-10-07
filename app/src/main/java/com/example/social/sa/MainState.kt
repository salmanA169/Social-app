package com.example.social.sa

import androidx.compose.runtime.Immutable

@Immutable
data class MainState(
    val shouldNavigateLoginScreen: Boolean = false,
    val imageProfile:String = ""
)