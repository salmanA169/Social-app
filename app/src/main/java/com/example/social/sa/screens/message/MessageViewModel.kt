package com.example.social.sa.screens.message

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.messageRepo.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val dispatcherProvider: DispatcherProvider
):ViewModel() {

    private val _state = MutableStateFlow(MessageState())
    val state = _state.asStateFlow()

    fun addChatId(chatId:String){
        viewModelScope.launch(dispatcherProvider.io) {
            messageRepository.addChatId(chatId)
            observeMessage()
        }
    }


    private fun observeMessage(){
        viewModelScope.launch(dispatcherProvider.io){
            messageRepository.observeMessages().collect{
                _state.value = _state.value.copy(messages = it)
            }
        }
    }
    fun onEvent(event: MessageScreenEvent){
        when(event) {
            is MessageScreenEvent.SendMessage -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    messageRepository.addMessage(event.text, event.mediaUri)
                }
            }
        }
    }
}

sealed class MessageScreenEvent{
    data class SendMessage(val text:String,val mediaUri: Uri?):MessageScreenEvent()
}