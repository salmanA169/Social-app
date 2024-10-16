package com.example.social.sa.core.fcm

import android.util.Log
import com.example.social.sa.core.database.Collections
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SocialFCM : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Firebase.auth.currentUser?.let {
            Firebase.firestore.collection(Collections.USERS_COLLECTIONS)
                .whereEqualTo("userUUID", it.uid).get().addOnCompleteListener {
                    it.result.first()!!.reference.update("token", token)
            }
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            Log.d("SocialFCM", "onMessageReceived: called message ${it.title} , ${it.body}")
        }
    }
}