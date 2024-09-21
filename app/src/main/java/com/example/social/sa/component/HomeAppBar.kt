package com.example.social.sa.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    image: String
) {
    TopAppBar(title = { }, navigationIcon = {
        AsyncImage(
            model = image, contentDescription = "", modifier = Modifier
                .size(48.dp)
                .clip(
                    CircleShape
                ),
            contentScale = ContentScale.Crop
        )
    })
}