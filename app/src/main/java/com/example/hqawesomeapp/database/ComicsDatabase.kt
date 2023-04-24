package com.example.hqawesomeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hqawesomeapp.dao.ComicDao
import com.example.hqawesomeapp.dao.ImageDao
import com.example.hqawesomeapp.dao.TextObjectDao
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.Image
import com.example.hqawesomeapp.data.TextObject

@Database(
    entities = [Comic::class, Image::class, TextObject::class],
    version = 1,
    exportSchema = false
)
abstract class ComicsDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun textObjectDao(): TextObjectDao
    abstract fun comicDao(comicsDatabase: ComicsDatabase): ComicDao

    companion object{
        @Volatile
        private var instance: ComicsDatabase? = null

        fun getDatabase(context: Context): ComicsDatabase {
            return instance ?: synchronized(this) {
                val dataBase = Room.databaseBuilder(
                    context.applicationContext,
                    ComicsDatabase::class.java,
                    "comic_data_base"
                ).build()
                this.instance = dataBase
                return dataBase
            }
        }
    }
}