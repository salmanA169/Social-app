package com.example.social.sa.screens.message

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.social.sa.model.ChatInfoState
import com.example.social.sa.model.Message

@Immutable
data class MessageState(
    val messages :List<Message> = emptyList(),
    val chatInfo:ChatInfoState?= null
){
    @Composable
    fun getMessagesItemColors(isMessageFromMe:Boolean) :MessageItemColors{
        return if (isMessageFromMe){
            MessageItemColors(
                MaterialTheme.colorScheme.onPrimaryContainer,
                MaterialTheme.colorScheme.primaryContainer
            )
        }else{
            MessageItemColors(
                MaterialTheme.colorScheme.onSurfaceVariant,
                MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
@Immutable
data class MessageItemColors(
    val contentColor: Color,
    val backgroundColor:Color
)

