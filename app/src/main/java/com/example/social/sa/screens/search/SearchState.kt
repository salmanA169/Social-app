package com.example.social.sa.screens.search

import androidx.compose.runtime.Immutable
import com.example.social.sa.model.UserInfo

@Immutable
data class SearchState(
    val query:String = "",
    val users : List<UserInfo> = emptyList()
)
