package com.example.hqawesomeapp.dao

import androidx.room.*
import com.example.hqawesomeapp.data.Image

@Dao
interface ImageDao: BaseDao<Image> {

    @Query("SELECT * FROM image")
    suspend fun getAllImages(): List<Image>?

    @Query("SELECT * FROM image WHERE imageId = :imageId")
    suspend fun getImage(imageId: Int): Image?

}