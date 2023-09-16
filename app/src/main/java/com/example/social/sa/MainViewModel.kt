package com.example.social.sa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.auth.UserSession
import com.example.social.sa.coroutine.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSession: UserSession,
    private val dispatcherProvider : DispatcherProvider
):ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        userSession.registerAuthListener()
        viewModelScope.launch(dispatcherProvider.io) {
            userSession.isAuth.collect{isAuth->
                _state.update {
                    it.copy(
                        shouldNavigateLoginScreen = isAuth == null
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userSession.removeAuthListener()
    }
}