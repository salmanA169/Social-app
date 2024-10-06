package com.example.social.sa.model

import com.example.social.sa.model_dto.MessageStatus
import com.google.firebase.Timestamp

data class Message(
    val messageId:String,
    val senderUID:String? = null,
    val content:String? = null,
    val image:String? = null,
    // TODO: change it to formatted data time
    val timestamp:Timestamp = Timestamp.now(),
    val status:MessageStatus = MessageStatus.LOADING,
    val messageType: MessageType,

){
    fun isMessageFromMe(myUid:String):Boolean{
        return senderUID == myUid
    }
}
enum class MessageType{
    TEXT,
    IMAGE,
    VIDEO
}
data class ChatInfoState(
    val chatId:String,
    val displayName:String,
    val imageUri:String,
    val lastMessage:String,
    val isLastMessageFromMe:Boolean
)