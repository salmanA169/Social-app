package com.example.social.sa.core.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserSession@Inject constructor(
    private val auth: FirebaseAuth
):FirebaseAuth.AuthStateListener {

    private var userEvent :UserSessionEvent? = null
    override fun onAuthStateChanged(p0: FirebaseAuth) {
       userEvent?.onUserDataUpdate(p0.currentUser.run {
           UserSessionInfo(
               uid = this?.uid ?: "",
               image = this?.photoUrl.toString(),
               shouldNavigate = p0.currentUser == null
           )
       })
    }

    fun addUserEventListener(userEvent:UserSessionEvent){
        this.userEvent = userEvent
    }
    fun registerAuthListener() {
        auth.addAuthStateListener(this)
    }

    fun getSignedInUser(): UserSessionInfo? {
        return auth.currentUser?.run {
            UserSessionInfo(
                uid, this.photoUrl.toString()
            )
        }
    }

    fun removeAuthListener() {
        auth.removeAuthStateListener(this)
    }
}
interface UserSessionEvent {
    fun onUserDataUpdate(userInfo: UserSessionInfo)
}
data class UserSessionInfo(
    val uid: String,
    val image: String,
    val shouldNavigate : Boolean = false
)