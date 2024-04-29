package com.example.hqawesomeapp.repository

import android.content.Context
import android.util.Log
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.dataSource.HqApiClientDataSource
import com.example.hqawesomeapp.dataSource.HqDataBaseDataSource

class HqRepository(context: Context) {

    companion object{
        const val TAG = "HqRepository"
    }

    private val hqApiClientDataSource = HqApiClientDataSource()
    private val hqDataBaseDataSource = HqDataBaseDataSource(context)

    suspend fun getHqData(): Result<List<Comic>?>{
        return try {
            val result = hqApiClientDataSource.getHqData()
            if (result.isSuccess){
                Log.d(TAG, "sucesso")
                persistData(result.getOrNull())
                result

            } else{
                Log.d(TAG, "falha")
                getLocalData()
            }
        } catch (e: Exception){
            Log.d(TAG, "falha")
            getLocalData()
        }
    }

    private suspend fun persistData(comicList: List<Comic>?){
        hqDataBaseDataSource.clearData()
        comicList?.let {
            hqDataBaseDataSource.saveData(it)
        }
    }

    private suspend fun getLocalData() = hqDataBaseDataSource.getHqData()

}