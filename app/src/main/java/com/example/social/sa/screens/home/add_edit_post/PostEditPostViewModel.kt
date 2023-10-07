package com.example.social.sa.screens.home.add_edit_post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.core.FileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostEditPostViewModel @Inject constructor(
    private val fileManager: FileManager
):ViewModel() {

    private val _state = MutableStateFlow(AddEditPostState())
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val imagtes =  fileManager.loadFilesExternalStorage().take(5)
            _state.update {
                it.copy(
                    imagtes
                )
            }
        }
    }
}