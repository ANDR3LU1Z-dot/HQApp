package com.example.hqawesomeapp

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class ApiHelper {
    companion object{
        fun getCurrentTimeStamp() = Timestamp(System.currentTimeMillis()).toString()

        fun generateMD5Hash(input: String): String{
            val md = MessageDigest.getInstance("MD5")
            val hash = md.digest(input.toByteArray())
            return BigInteger(1, hash).toString(16).padStart(32,'0')
        }
    }
}