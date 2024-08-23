package com.example.social.sa.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    image: String
) {
    TopAppBar(title = { }, navigationIcon = {
        AsyncImage(model = image, contentDescription = "", modifier = Modifier.size(32.dp))
    })
}