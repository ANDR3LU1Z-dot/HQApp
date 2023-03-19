package com.example.hqawesomeapp.data

import com.squareup.moshi.JsonClass
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@JsonClass(generateAdapter = true)
data class Comic(
    val id: Int?,
    val title: String?,
    val description: String?,
    val textObject: List<TextObject>?,
    val thumbnail: Image?,
    val images: List<Image>?
){
    fun getContent(): String{
        return when {
            description?.isNotEmpty() == true -> description
            textObject?.isNotEmpty() == true -> textObject[0].text ?: "Conteúdo não disponível"
            else -> "Conteúdo não disponível"
        }
    }

    fun getIdString(): String{
        return id?.toString() ?: ""
    }

    fun getImageUrl():String? = thumbnail?.getFullImagePath()

    fun getCarouselImages(): List<CarouselItem>? = images?.map {
        CarouselItem(imageUrl = it.getFullImagePath())
    }
}
