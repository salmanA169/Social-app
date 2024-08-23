package com.example.social.sa.utils

import kotlin.time.Duration

fun Duration.formatSecondAndMinute() = toComponents { minutes, seconds, nanoseconds ->
    "%02d:%02d".format(minutes, seconds)
}