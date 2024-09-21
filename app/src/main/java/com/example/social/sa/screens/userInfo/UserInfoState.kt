package com.example.social.sa.screens.userInfo

import androidx.compose.runtime.Immutable
import com.example.social.sa.model.Posts
import com.example.social.sa.model.UserInfo

@Immutable
data class UserInfoState(
    val userInfo: UserInfo?= null,
    val postsUser:List<Posts> = emptyList(),
    val isFollowing:Boolean = false
)
