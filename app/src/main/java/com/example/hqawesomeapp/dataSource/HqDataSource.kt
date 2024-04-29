package com.example.hqawesomeapp.dataSource

import com.example.hqawesomeapp.data.Comic

interface HqDataSource {
    suspend fun getHqData(): Result<List<Comic>?>

    suspend fun saveData(comicList: List<Comic>)

    suspend fun clearData()
}