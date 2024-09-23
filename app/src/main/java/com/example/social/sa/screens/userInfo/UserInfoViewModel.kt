package com.example.social.sa.screens.userInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.messageRepo.MessageRepository
import com.example.social.sa.repository.postRepository.PostRepository
import com.example.social.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val postRepository: PostRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserInfoState())
    val state = _state.asStateFlow()
    private val _effect = MutableStateFlow<UserInfoEffect?>(null)
    val effect = _effect.asStateFlow()
    fun resetEffect(){
        _effect.update {
            null
        }
    }
    private fun getPosts(userUUId: String) {
        viewModelScope.launch {
            postRepository.getPostsByUserUUID(userUUId).also { posts ->
                _state.update {
                    it.copy(
                        postsUser = posts
                    )
                }
            }
        }
    }

    fun onEvent(userInfoEvent: UserInfoEvent){
        when(userInfoEvent){
            UserInfoEvent.MessageClick -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val otherUserInfo = state.value.userInfo
                    val checkChatIfExist = messageRepository.getChatIfExist(otherUserInfo!!.userUUID)
                    if (checkChatIfExist!= null ){
                        _effect.update {
                            UserInfoEffect.NavigateToMessageScreen(checkChatIfExist)
                        }
                    }else{
                        val createNewChat = messageRepository.createNewChat(otherUserInfo.userUUID)
                        _effect.update {
                            UserInfoEffect.NavigateToMessageScreen(createNewChat)
                        }
                    }
                }
            }
            UserInfoEvent.RequestFollow->{
                viewModelScope.launch(dispatcherProvider.io) {
                    userRepository.requestFollow(_state.value.userInfo!!.userUUID)
                }
            }

            UserInfoEvent.UnFollowEvent ->{
                viewModelScope.launch(dispatcherProvider.io) {
                    userRepository.unFollowRequest(_state.value.userInfo!!.userUUID)
                }
            }
        }
    }
    private suspend fun getFollowUser(userUid:String){
        userRepository.getFollowUser(userUid).apply {
            _state.update {
                it.copy(
                    userInfo = it.userInfo?.copy(
                        followers = this.followers,
                        following = this.following,
                        postsCount = this.postCount
                    ),
                    isFollowing = this.isFollowing
                )
            }
        }
    }
    fun getUserInfo(userUUID: String) {
        getPosts(userUUID)

        viewModelScope.launch(dispatcherProvider.io) {
            userRepository.getUserInfoByUUID(userUUID).also { userInfo ->
                _state.update {
                    it.copy(
                        userInfo
                    )
                }
            }
            getFollowUser(userUUID)
        }
    }
}
sealed class UserInfoEffect{
    data class NavigateToMessageScreen(val chatId:String):UserInfoEffect()
}
sealed class UserInfoEvent{
    data object MessageClick:UserInfoEvent()
    data object RequestFollow:UserInfoEvent()
    data object UnFollowEvent:UserInfoEvent()
}