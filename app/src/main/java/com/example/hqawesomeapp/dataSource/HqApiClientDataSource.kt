package com.example.hqawesomeapp.dataSource

import android.util.Log
import com.example.hqawesomeapp.api.ComicService
import com.example.hqawesomeapp.data.ApiCredentials
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.helper.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HqApiClientDataSource: HqDataSource {

    companion object{
        const val TAG = "HqApiClientDataSource"
    }

        private val retrofit = Retrofit.Builder()
        .baseUrl(ApiCredentials.baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

        private val comicService = retrofit.create(ComicService::class.java)

    override suspend fun getHqData(): Result<List<Comic>?> =
        withContext(Dispatchers.IO) {
            val timeStamp = ApiHelper.getCurrentTimeStamp()
            val input = "$timeStamp${ApiCredentials.privateKey}${ApiCredentials.publicKey}"
            val hash = ApiHelper.generateMD5Hash(input)
            val response = comicService.getComicList(timeStamp, ApiCredentials.publicKey, hash, 100)

            when {
                response.isSuccessful -> {
                    Log.d(TAG, "Sucesso")
                    Result.success(response.body()?.data?.results)
                }
                else -> {
                    Log.d(TAG, "Falha")
                    Result.failure(Throwable(response.message()))
                }
            }

        }

    override suspend fun saveData(comicList: List<Comic>) {
        //NO-OP
    }

    override suspend fun clearData() {
        //NO-OP
    }


}