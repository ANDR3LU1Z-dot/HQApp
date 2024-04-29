package com.example.hqawesomeapp.dataSource

import android.content.Context
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.ComicWithAllProperties
import com.example.hqawesomeapp.database.ComicsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HqDataBaseDataSource(
    context: Context
): HqDataSource {

    private val comicsDatabase = ComicsDatabase.getDatabase(context)
    private val comicDao = comicsDatabase.comicDao(comicsDatabase)

    override suspend fun getHqData(): Result<List<Comic>?> =
        withContext(Dispatchers.IO){
            Result.success(loadPersistedComicData())
        }

    override suspend fun saveData(comicList: List<Comic>) {
        comicDao.insertComicList(comicList)
    }

    override suspend fun clearData() {
        comicDao.clearComicData()
    }

    private suspend fun loadPersistedComicData() = comicDao.getAllComics()?.map {
        mapComicWithPropertiesToComic(it)
    }

    private fun mapComicWithPropertiesToComic(comicWithAllProperties: ComicWithAllProperties): Comic{
        comicWithAllProperties.comic.images = comicWithAllProperties.images
        comicWithAllProperties.comic.textObject = comicWithAllProperties.textObject
        return comicWithAllProperties.comic
    }
}