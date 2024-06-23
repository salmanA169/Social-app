package com.example.social.sa.screens.home.add_edit_post

import com.example.social.sa.core.MediaTypeData

data class AddEditPostState(
    val images:List<MediaTypeData> = emptyList(),
    val pickedImage:List<MediaTypeData> = emptyList()
)
