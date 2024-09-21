package com.example.social.sa.model_dto

import com.example.social.sa.model.Message
import com.example.social.sa.model.MessageType
import com.google.firebase.Timestamp

data class ChatRoomDto(
    val participants:List<String> = emptyList(),
    val chatRoomType :ChatRoomType = ChatRoomType.PRIVATE,
    val lastMessage:String? = null,
    val lastMessageSender:String? = null,
    val lastMessageTimestamp: Timestamp = Timestamp.now(),
    val chatRoomId:String? = null,
)

data class MessageDto(
    val messageId:String = "",
    val senderUID:String? = null,
    val content:String? = null,
    val image:String? = null,
    val timestamp:Timestamp = Timestamp.now(),
    val status:MessageStatus = MessageStatus.LOADING,
    val messageType: MessageType = MessageType.TEXT
)
fun MessageDto.toMessage():Message{
    return Message(
        messageId,senderUID,content,image,timestamp,status, messageType,)
}
fun List<MessageDto>.toMessages():List<Message>{
    return this.map { it.toMessage() }
}
enum class MessageStatus{
    LOADING,SENT,DELIVERED,SEEN;
}
enum class ChatRoomType{
    PRIVATE,GROUP
}