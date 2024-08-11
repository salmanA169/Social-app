package com.example.social.sa.screens.register

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.Screens
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.registerRepository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: RegisterRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<RegisterEffect?>(null)
    val effect = _effect.asStateFlow()
    fun onEvent(registerEvent: RegisterEvent) {
        when (registerEvent) {
            is RegisterEvent.EmailDataChange -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(content = registerEvent.email)
                    )
                }
            }

            RegisterEvent.ForgetPassword -> TODO()
            RegisterEvent.GoogleSign -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getGoogleSignIn = userRepository.getGoogleAuthResult()
                    _state.update {
                        it.copy(
                            googleIntentSender = getGoogleSignIn
                        )
                    }
                }
            }
            is RegisterEvent.PasswordDataChange -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(content = registerEvent.password)
                    )
                }
            }
            is RegisterEvent.GoogleSignInResult -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getResult = userRepository.signInGoogle(registerEvent.intent)
                    if (getResult.isSuccess) {
                        _effect.update {
                            RegisterEffect.Navigate(Screens.HomeScreen.route)
                        }
                    }

                }
            }

            is RegisterEvent.RegisterTypeChange -> {
                val currentRegisterType = _state.value.registerType
                if (currentRegisterType != registerEvent.registerType) {
                    _state.update {
                        it.copy(
                            registerType = registerEvent.registerType,
                            email = InputData(),
                            password = InputData()
                        )
                    }
                }
            }

            RegisterEvent.SignIn -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val state = _state.value
                    val signResult = userRepository.signInEmailAndPassword(
                        state.email.content,
                        state.password.content
                    )

                    if (state.email.content.isEmpty() || state.password.content.isEmpty()) {
                        _state.update {
                            it.copy(
                                email = it.email.copy(
                                    isError = state.email.content.isEmpty(),
                                    errorText = if (state.email.content.isEmpty()) "Email can not be empty" else ""
                                ),
                                password = it.password.copy(
                                    isError = state.password.content.isEmpty(),
                                    errorText = if (state.password.content.isEmpty()) "Password can not be empty" else ""
                                )
                            )
                        }
                        return@launch
                    }
                    resetContent()
                    if (signResult.isSuccess) {
//                        for test now
                        _effect.update {
                            RegisterEffect.Navigate(Screens.HomeScreen.route)
                        }

                    } else {
                        _effect.update {
                            RegisterEffect.ToastError(signResult.error ?: "Unknown Error")
                        }
                    }
                }
            }

            RegisterEvent.SignUp ->{
                val getEmail = _state.value.email.content
                _effect.update {
                    RegisterEffect.NavigateToInfoRegister(getEmail,null,null)
                }
            }
        }
    }

    private fun resetContent() {
        _state.update {
            it.copy(
                email = it.email.copy(isError = false, errorText = ""),
                password = it.password.copy(isError = false, errorText = ""),
            )
        }
    }

    private fun emailErrorMessage(message: String) {
        _state.update {
            it.copy(
                email = it.email.copy(isError = true, errorText = message)
            )
        }
    }

    private fun passwordErrorMessage(message: String) {
        _state.update {
            it.copy(
                password = it.password.copy(isError = true, errorText = message)
            )
        }
    }

    fun resetEffect() {
        _effect.update {
            null
        }
    }
}

sealed class RegisterEffect {
    class Navigate(val route: String) : RegisterEffect()
    class ToastError(val message: String) : RegisterEffect()
    class NavigateToInfoRegister(val email: String,val userName:String?,val imageUrl:String?) : RegisterEffect()
}

sealed class RegisterEvent {
    data class RegisterTypeChange(val registerType: RegisterType) : RegisterEvent()
    data class EmailDataChange(val email: String) : RegisterEvent()
    data class PasswordDataChange(val password: String) : RegisterEvent()
    object SignIn : RegisterEvent()
    object ForgetPassword : RegisterEvent()
    object SignUp : RegisterEvent()
    object GoogleSign : RegisterEvent()
    data class GoogleSignInResult(val intent:Intent):RegisterEvent()
}