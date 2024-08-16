package com.example.social.sa.screens.register.info_register

import android.icu.text.IDNA.Info
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.model.UserInfo
import com.example.social.sa.repository.registerRepository.RegisterRepository
import com.example.social.sa.screens.register.InputData
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoRegisterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val registerRepository: RegisterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(InfoRegisterState())
    val state = _state.asStateFlow()


    private val _effect = MutableStateFlow<InfoRegisterEffect?>(null)
    val effect = _effect.asStateFlow()

    private val query = MutableStateFlow<String>("")

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            query.debounce(500)
                .distinctUntilChanged()
                .collectLatest { queryFlow ->
                    if (queryFlow.isEmpty()) {
                        return@collectLatest
                    }
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val getResponse = registerRepository.checkUserNameAvailable(queryFlow)
                    if (!getResponse.isAvailable) {
                        _state.update {
                            it.copy(
                                userName = it.userName.copy(
                                    errorText = getResponse.errorMessage ?: "UnKnown Error",
                                    isError = true
                                ),
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                userName = it.userName.copy(isError = false),
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    fun onEvent(event: InfoRegisterEvent) {
        when (event) {
            is InfoRegisterEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = InputData(event.email))
            }

            is InfoRegisterEvent.NameChanged -> {
                _state.value = _state.value.copy(displayName = InputData(event.name))

            }


            InfoRegisterEvent.Submit -> {
                _state.update {
                    it.copy(isLoading = true)
                }
                viewModelScope.launch(dispatcherProvider.io) {
                    val value = _state.value
                    val inputsData = listOf(value.email, value.userName, value.displayName)
                    if (inputsData.all {
                        it.content.isNotBlank()
                        }){
                        registerRepository.signUpUser(
                            value.email.content,
                            value.userName.content,
                            value.imageUri?:"",
                            value.displayName.content
                        )
                        _effect.update {
                            InfoRegisterEffect.NavigateHome
                        }
                    }else{
                        inputsData.forEach {
                            if (it.content.isEmpty()){
                                it.setError("Must not be empty")
                            }
                        }
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }

            is InfoRegisterEvent.UserNameChanged -> {
                _state.update {
                    it.copy(
                        userName = InputData(event.string)
                    )
                }
                query.value = event.string
            }

            is InfoRegisterEvent.ImageChanged -> {
                _state.update {
                    it.copy(
                        imageUri = event.uri
                    )
                }
            }

            is InfoRegisterEvent.ArgChanges -> {
                _state.update {
                    it.copy(
                        email = InputData(event.email),
                        displayName = InputData(event.name ?: ""),
                        imageUri = event.image
                    )
                }
            }
        }
    }

    fun resetEffect(){
        _effect.update {
            null
        }
    }
}

sealed class InfoRegisterEffect {
    data object NavigateHome : InfoRegisterEffect()
}

sealed class InfoRegisterEvent {
    data class NameChanged(val name: String) : InfoRegisterEvent()
    data class EmailChanged(val email: String) : InfoRegisterEvent()
    data object Submit : InfoRegisterEvent()
    data class UserNameChanged(val string: String) : InfoRegisterEvent()
    data class ImageChanged(val uri: String) : InfoRegisterEvent()
    data class ArgChanges(val email: String, val name: String?, val image: String?) :
        InfoRegisterEvent()
}