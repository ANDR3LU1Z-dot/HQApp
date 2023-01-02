package com.example.hqawesomeapp.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comic(
    val id: Int?,
    val title: String?,
    val description: String?,
    val thumbnail: Image?
)
