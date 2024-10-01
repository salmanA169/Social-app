package com.example.social.sa.screens.inbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.inboxRepo.InboxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
    private val dispatcherProvider: DispatcherProvider
) :ViewModel(){
    private val _inboxState = MutableStateFlow(InboxState())
    val inboxState = _inboxState.asStateFlow()
    private var job: Job? = null
    init {
        job?.cancel()
        job = viewModelScope.launch(dispatcherProvider.io) {
            inboxRepository.observeChats().collect{chats->
                Log.d("InboxViewModel", "called chat observe: $chats")
                _inboxState.update {
                    it.copy(
                        chats
                    )
                }
            }
        }
    }
    fun stopObserve(){
        // TODO: for test now when back screen not observe again
       job?.cancel()
    }
}