package com.example.social.sa.utils

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


fun Timestamp.format():String{
    val postDate = LocalDateTime.ofInstant(toDate().toInstant(), ZoneId.systemDefault())
    val currentDate = LocalDateTime.now()
    return currentDate.until(postDate,ChronoUnit.SECONDS).toString()
}
fun Long.format():String{
    val timeStampDate = Timestamp(this,0)
    return "1d"
}