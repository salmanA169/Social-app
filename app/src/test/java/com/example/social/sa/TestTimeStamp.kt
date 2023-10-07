package com.example.social.sa

import com.example.social.sa.utils.format
import com.google.firebase.Timestamp
import org.junit.Test

class TestTimeStamp {
    @Test
    fun testTimeStampToLocalDateTime(){
        println(Timestamp.now().format())
    }
}