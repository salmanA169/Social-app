package com.example.social.sa.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark

@Composable
fun RegisterButton(
    text:String,
    onClick:() -> Unit,
    modifier :Modifier = Modifier,
    enabled:Boolean = true
) {
    Button(onClick = onClick,modifier = modifier, shape = RoundedCornerShape(20f), enabled = enabled) {
        Text(text = text, fontSize = 19.sp,modifier = Modifier.padding(vertical = 6.dp))
    }
}

@PreviewBothLightAndDark
@Composable
fun RegisterButtonPrev() {
    SocialTheme {
        RegisterButton(text = "Sign in", onClick = { /*TODO*/ })
    }
}