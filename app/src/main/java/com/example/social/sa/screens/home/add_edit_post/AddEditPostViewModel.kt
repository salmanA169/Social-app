package com.example.social.sa.screens.home.add_edit_post

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.social.sa.core.FileManager
import com.example.social.sa.core.MediaTypeData
import com.example.social.sa.coroutine.DispatcherProvider
import com.example.social.sa.repository.postRepository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditPostViewModel @Inject constructor(
    private val fileManager: FileManager,
    private val dispatcherProvider: DispatcherProvider,
    private val postRepository: PostRepository
):ViewModel() {

    private val _state = MutableStateFlow(AddEditPostState())
    val state = _state.asStateFlow()
    private val _effect = MutableStateFlow<AddEditPostEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val images =  fileManager.loadFilesExternalStorage()
            _state.update {
                it.copy(
                    images
                )
            }
        }
    }

    fun onEvent(event: AddEditPostEvent){
        when(event){
            is AddEditPostEvent.PickImage -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val media = fileManager.getMedia(event.imageUri.toUri())
                    val state = _state.value
                    if (state.pickedImage.find { it.id == media.id }==null ){
                        _state.update {
                            it.copy(
                                pickedImage = it.pickedImage + media
                            )
                        }
                    }
                }
            }


            is AddEditPostEvent.DeleteImage -> {
                val state = _state.value
                if (state.pickedImage.contains(event.currentMedia)) {
                    _state.update {
                        it.copy(
                            pickedImage = it.pickedImage - event.currentMedia
                        )
                    }
                }

            }


            is AddEditPostEvent.Navigate -> {
                _effect.update {
                    AddEditPostEffect.Navigate(event.route)
                }
            }

            is AddEditPostEvent.PickedFromCamera ->{
                val state = _state.value
                if (!state.pickedImage.contains(event.mediaType)){
                    _state.update {
                        it.copy(
                            pickedImage = it.pickedImage + event.mediaType
                        )
                    }
                }
            }

            is AddEditPostEvent.SendPost -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    postRepository.sendPost(event.text,_state.value.pickedImage.map { it.uri }).also {
                        it.error?.let { errorMessage->
                            Log.e("AddEditPostViewModel", "onEvent: called error $errorMessage")
                        }
                    }
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
sealed class AddEditPostEffect{
    class Navigate(val route:Any):AddEditPostEffect()
}
sealed class AddEditPostEvent{
    class PickImage(val imageUri:String):AddEditPostEvent()
    class DeleteImage(val currentMedia:MediaTypeData):AddEditPostEvent()
    class Navigate(val route:Any):AddEditPostEvent()
    data class PickedFromCamera(val mediaType: MediaTypeData):AddEditPostEvent()
    data class SendPost(val text:String):AddEditPostEvent()
}