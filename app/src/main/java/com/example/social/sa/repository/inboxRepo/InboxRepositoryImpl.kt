package com.example.social.sa.repository.inboxRepo

import android.util.Log
import com.example.social.sa.core.requests.FireStoreRequests
import com.example.social.sa.model.ChatInfoState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor(
    private val socialFireStoreRequests: FireStoreRequests,
    private val auth: FirebaseAuth
):InboxRepository {
    override fun observeChats(): Flow<List<ChatInfoState>> {
        val result = socialFireStoreRequests.observeChats()
        if (result.isSuccess){
            return result.data!!.map {chats->
                chats.map {
                    val filterUser = it.participants.firstOrNull{users-> users!= auth.currentUser!!.uid}?:"bsyLTZJ4ujOR8sSPLT7l5IQ56hs1"
                    val getUserInfo = socialFireStoreRequests.getUserInfoByUUID(filterUser).data!!
                    ChatInfoState(
                        displayName = getUserInfo.displayName,
                        imageUri = getUserInfo.imageUri,
                        lastMessage = it.lastMessage?:"",
                        isLastMessageFromMe = it.lastMessageSender == auth.currentUser!!.uid
                    )
                }
            }
        }else{
            throw Exception("Can not observe chat")
        }
    }
}