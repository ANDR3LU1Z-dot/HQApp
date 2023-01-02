package com.example.hqawesomeapp.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    val path: String?,
    val extension: String?
)
