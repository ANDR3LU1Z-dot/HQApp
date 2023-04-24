package com.example.hqawesomeapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(
    foreignKeys = [
            ForeignKey(
                entity = Comic::class,
                parentColumns = ["id"],
                childColumns = ["comicId"],
                onDelete = ForeignKey.CASCADE
            )
    ]
)
@JsonClass(generateAdapter = true)
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int?,
    val path: String?,
    val extension: String?,
    @ColumnInfo(index = true)
    var comicId: Int?
){
    fun getFullImagePath(): String {
        val pathHttps = path?.replace("http", "https")
        return "$pathHttps.$extension"
    }
}
