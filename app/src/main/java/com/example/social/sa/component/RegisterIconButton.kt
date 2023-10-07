package com.example.social.sa.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.social.sa.R
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark


@Composable
fun RegisterIconButton(
    icon:Painter,
    modifier: Modifier = Modifier ,
    onClick:()->Unit
) {
    OutlinedButton(onClick = onClick,modifier = modifier, shape = CircleShape, contentPadding = PaddingValues(24.dp)) {
        Icon(painter = icon, contentDescription = "")
    }
}


@PreviewBothLightAndDark
@Composable
fun RegisterIconButtonPrev() {
    SocialTheme {
        RegisterIconButton(icon = painterResource(id = R.drawable.google_icon)) {

        }
    }
}