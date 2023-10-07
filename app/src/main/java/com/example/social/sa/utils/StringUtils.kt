package com.example.social.sa.utils

import android.util.Patterns
import java.util.regex.Pattern


val CharSequence.isEmailValid :Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()