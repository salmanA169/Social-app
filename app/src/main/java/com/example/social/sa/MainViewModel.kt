package com.example.social.sa

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.auth.UserSession
import com.example.social.sa.coroutine.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSession: UserSession,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        userSession.registerAuthListener()
        viewModelScope.launch(dispatcherProvider.io) {
            userSession.isAuth.collectLatest { isAuth ->
                delay(1000)
                _state.update {
                    it.copy(
                        // TODO: for test now auto == null 
                        shouldNavigateLoginScreen = false,
                        imageProfile = isAuth?.image ?: ""
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