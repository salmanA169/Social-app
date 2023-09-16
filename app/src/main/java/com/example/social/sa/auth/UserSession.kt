package com.example.social.sa.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserSession@Inject constructor(
    private val auth: FirebaseAuth
):FirebaseAuth.AuthStateListener {

    private val _isAuth = MutableStateFlow<String?>(null)
    val isAuth = _isAuth.asStateFlow()
    override fun onAuthStateChanged(p0: FirebaseAuth) {
        _isAuth.update {
            p0.currentUser?.uid
        }
    }

    fun registerAuthListener(){
        auth.addAuthStateListener(this)
    }

    fun removeAuthListener(){
        auth.removeAuthStateListener(this)
    }
}