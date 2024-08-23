package com.example.social.sa.utils

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


fun Timestamp.format(): String {
    val postDate = LocalDateTime.ofInstant(toDate().toInstant(), ZoneId.systemDefault())
    val currentDate = LocalDateTime.now()
    return currentDate.until(postDate, ChronoUnit.SECONDS).toString()
}

fun Timestamp.toLocalDateTime(): LocalDateTime{
    val instant = toInstant()
    val toLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return toLocalDateTime
}
fun Long.format(): String {
    val timeStampDate = Timestamp(this, 0)
    return "1d"
}