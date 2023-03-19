package com.example.hqawesomeapp.api

import com.example.hqawesomeapp.data.ComicResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicService {

    @GET("v1/public/comics")
    suspend fun getComicList(
        @Query("ts") timestamp: String,
        @Query("apikey") publicKey: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int
    ) : Response<ComicResponse>
}