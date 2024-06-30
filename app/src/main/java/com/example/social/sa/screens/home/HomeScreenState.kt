package com.example.social.sa.screens.home

import androidx.compose.runtime.Immutable
import com.example.social.sa.R
import com.example.social.sa.model.Posts
@Immutable
data class HomeScreenState(
    val profileImage:String = "",
    var tabItem: TabItem = TabItem.HOME,
    val homePosts: List<Posts> = emptyList(),
    val forYouPosts:List<Posts> = emptyList(),
    val isLoading :Boolean = false
)

enum class TabItem(val tabName:Int){
    HOME(R.string.home),FOR_YOU(R.string.for_you)
}
