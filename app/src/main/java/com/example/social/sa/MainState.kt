package com.example.social.sa

import androidx.compose.runtime.Immutable

@Immutable
data class MainState(
    val isLoggedIn: Boolean = false,
    val imageProfile:String = "",
    val startDestination: StartDestinationStatus = StartDestinationStatus.LOADING,
    val startDestinationRoute:String?= null
)

enum class StartDestinationStatus{
    LOADING,SUCCESS
}

