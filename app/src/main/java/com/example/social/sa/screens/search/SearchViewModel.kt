package com.example.social.sa.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.model.UserInfo
import com.example.social.sa.model.doesMatchSearchQuery
import com.example.social.sa.repository.userRepository.UserRepository
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val userRepo: UserRepository
) : ViewModel() {
    private val _effect = MutableStateFlow<SearchEffect?>(null)
    val effect = _effect.asStateFlow()

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val users = MutableStateFlow<List<UserInfo>>(emptyList())
    private val query = MutableStateFlow("")
    private val usersResult = query
        .debounce(500)
        .combine(users){query,state->
            if(query.isBlank()){
                emptyList()
            }else{
                state.filter {
                    it.doesMatchSearchQuery(query)
                }
            }
        }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getUsers = userRepo.getAllUsers()
            users.update {
               getUsers
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            usersResult.collect{users->
                _state.update {
                    it.copy(
                        users = users
                    )
                }
            }
        }
    }
    fun onEvent(event: SearchEvent) {
        when(event){
            SearchEvent.PopBack -> {
                _effect.value = SearchEffect.PopBack
            }
            is SearchEvent.Query ->{
                _state.update {
                    it.copy(
                        query = event.query
                    )
                }
                query.update {
                    event.query
                }

            }

            is SearchEvent.NavigateUserProfile -> {
                _effect.value = SearchEffect.NavigateUserProfile(event.userUUID)
            }
        }
    }
    fun resetEffect(){
        _effect.value = null
    }
}
sealed class SearchEffect{
    data object PopBack:SearchEffect()
    data class NavigateUserProfile(val userUUID:String):SearchEffect()
}
sealed class SearchEvent {
    class Query(val query: String) : SearchEvent()
    data object PopBack : SearchEvent()
    data class NavigateUserProfile(val userUUID:String):SearchEvent()
}