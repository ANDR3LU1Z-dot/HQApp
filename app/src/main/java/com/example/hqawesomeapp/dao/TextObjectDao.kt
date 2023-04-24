package com.example.hqawesomeapp.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.hqawesomeapp.data.TextObject

@Dao
interface TextObjectDao: BaseDao<TextObject> {

    @Query("SELECT * FROM TextObject")
    suspend fun getAllTextObjects(): List<TextObject>?

    @Query("SELECT * FROM TextObject WHERE textObjectId = :textObjectId")
    suspend fun getTextObject(textObjectId: Int): TextObject?
}