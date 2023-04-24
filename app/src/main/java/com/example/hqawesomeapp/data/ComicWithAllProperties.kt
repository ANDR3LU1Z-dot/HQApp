package com.example.hqawesomeapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class ComicWithAllProperties (
    @Embedded var comic: Comic,
    @Relation(
        entity = Image::class,
        parentColumn = "id",
        entityColumn = "comicId"
    ) var images: List<Image>,
    @Relation(
        entity = TextObject::class,
        parentColumn = "id",
        entityColumn = "comicId"
    ) var textObject: List<TextObject>

)