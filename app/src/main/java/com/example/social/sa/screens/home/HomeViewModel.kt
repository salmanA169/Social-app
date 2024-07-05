package com.example.social.sa.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.postRepository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()


    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            postRepository.getPostsFlow().collect { posts ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        homePosts = posts
                    )
                }
            }
        }
    }
}
