package com.example.social.sa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.core.auth.UserSession
import com.example.social.sa.core.auth.UserSessionEvent
import com.example.social.sa.core.auth.UserSessionInfo
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.registerRepository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _effect = MutableStateFlow<MainEffect?>(null)
    val effect = _effect.asStateFlow()

    init {
        userSession.registerAuthListener()
        userSession.addUserEventListener(this)
        viewModelScope.launch (dispatcherProvider.io){
            // TODO: fix it to move in main activity directly
            val startDest = if (userSession.getSignedInUser() == null) Screens.RegisterScreen.route else Screens.HomeScreen.route
            _state.update {
                it.copy(
                    startDestination = StartDestinationStatus.SUCCESS,
                    startDestinationRoute = startDest
                )
            }
        }
    }

    override fun onUserDataUpdate(userInfo: UserSessionInfo) {
        _state.update {
            it.copy(
                isLoggedIn = userInfo.shouldNavigate,
                imageProfile = userInfo.image
            )
        }
    }

    fun resetEffect(){
        _effect.update {
            null
        }
    }
    fun onEvent(mainEvent: MainEvent){
        when(mainEvent){
            MainEvent.LogOut -> {
                signOut()
            }
        }
    }
    private fun signOut() {
        viewModelScope.launch(dispatcherProvider.io) {
            registerRepositoryImpl.signOut()
            _effect.update {
                MainEffect.NavigateToRegisterRoute
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userSession.removeAuthListener()
    }
}
sealed class MainEffect{
    data object NavigateToRegisterRoute:MainEffect()
}
sealed class MainEvent{
    data object LogOut: MainEvent()
}