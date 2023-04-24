package com.example.hqawesomeapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@Entity
@JsonClass(generateAdapter = true)
class Comic() {

    @PrimaryKey
    var id: Int? = null

    var title: String? = null

    var description: String? = null

    @Ignore
    var textObject: List<TextObject>? = null

    @Embedded
    var thumbnail: Image? = null

    @Ignore
    var images: List<Image>? = null

    @Ignore
    constructor(
        id: Int? = null,
        title: String? = null,
        description: String? = null,
        textObject: List<TextObject>? = null,
        thumbnail: Image? = null,
        images: List<Image>? = null
    ) : this() {
        this.id = id
        this.title = title
        this.description = description
        this.textObject = textObject
        this.thumbnail = thumbnail
        this.images = images
    }

    fun getContent(): String {
        val desc = description
        val text = textObject
        return when {
            desc?.isNotEmpty() == true -> desc
            text?.isNotEmpty() == true -> text[0].text ?: "Conteúdo não disponível"
            else -> "Conteúdo não disponível"
        }
    }

    fun getIdString(): String {
        return id?.toString() ?: ""
    }

    fun getImageUrl(): String? = thumbnail?.getFullImagePath()

    fun getCarouselImages(): List<CarouselItem>? = images?.map {
        CarouselItem(imageUrl = it.getFullImagePath())
    }
}
