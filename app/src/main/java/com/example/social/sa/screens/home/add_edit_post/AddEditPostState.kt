package com.example.social.sa.screens.home.add_edit_post

import com.example.social.sa.core.MediaType

data class AddEditPostState(
    val images:List<String> = emptyList(),
    val pickedImage:List<MediaType> = emptyList()
)
