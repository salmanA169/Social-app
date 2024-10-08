package com.example.social.sa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.core.auth.UserSession
import com.example.social.sa.core.auth.UserSessionEvent
import com.example.social.sa.core.auth.UserSessionInfo
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.registerRepository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSession: UserSession,
    private val dispatcherProvider: DispatcherProvider,
    private val registerRepositoryImpl: RegisterRepository
) : ViewModel(), UserSessionEvent {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        userSession.registerAuthListener()
        userSession.addUserEventListener(this)
    }

    override fun onUserDataUpdate(userInfo: UserSessionInfo) {
        _state.update {
            it.copy(
                shouldNavigateLoginScreen = userInfo.shouldNavigate,
                imageProfile = userInfo.image
            )
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcherProvider.io) {
            registerRepositoryImpl.signOut()
        }
    }

    override fun onCleared() {
        super.onCleared()
        userSession.removeAuthListener()
    }
}