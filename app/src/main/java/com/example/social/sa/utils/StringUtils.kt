package com.example.social.sa.utils

import android.util.Patterns


val CharSequence.isEmailValid :Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun generateID():String{
    return ('a'..'z').shuffled().take(10).joinToString("")
}