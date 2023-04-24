package com.example.hqawesomeapp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.ComicWithAllProperties
import com.example.hqawesomeapp.database.ComicsDatabase

@Dao
abstract class ComicDao(
    comicsDatabase: ComicsDatabase
): BaseDao<Comic> {

    private val imageDao = comicsDatabase.imageDao()
    private val textObjectDao = comicsDatabase.textObjectDao()

    @Transaction
    @Query("SELECT * FROM comic")
    abstract suspend fun getAllComics(): List<ComicWithAllProperties>?

    @Transaction
    @Query("SELECT * FROM comic WHERE id= :id")
    abstract suspend fun getComic(id: Int): ComicWithAllProperties?

    @Transaction
    @Query("DELETE FROM comic")
    abstract suspend fun clearComicData()

    @Transaction
    open suspend fun insertComicList(comicList: List<Comic>){
        comicList.forEach{insertComic(it)}
    }

    @Transaction
    open suspend fun insertComic(comic: Comic){
        comic.thumbnail?.comicId = comic.id
        comic.images?.forEach{
            it.comicId = comic.id
        }
        comic.textObject?.forEach{
            it.comicId = comic.id
        }

        insert(comic)
        comic.textObject?.let { textObjectDao.insertList(it) }
        comic.images?.let { imageDao.insertList(it) }
    }
}