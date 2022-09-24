package com.example.hqawesomeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hqawesomeapp.placeholder.PlaceholderContent
import com.example.hqawesomeapp.placeholder.PlaceholderContent.PlaceholderItem

class HQViewModel : ViewModel() {

    val hqDetailsLiveData: LiveData<HQDetails>
        get() = hqDetailsMTLiveData

    private val hqDetailsMTLiveData = MutableLiveData<HQDetails>()

    val hqListLiveData: LiveData<MutableList<PlaceholderItem>>
        get() = hqListMTLiveData

    private val hqListMTLiveData =
        MutableLiveData<MutableList<PlaceholderItem>>()

    val navigationToDetailsLiveData
        get() = navigationToDetailMTLiveData

    private val navigationToDetailMTLiveData  = MutableLiveData<Unit>()

    init {
        hqListMTLiveData.postValue(PlaceholderContent.ITEMS)
    }

    fun onHQSelected(position: Int) {
        val hqDetails = HQDetails("Minha HQ", "Este é apenas um conteudo de texto maior")
        hqDetailsMTLiveData.postValue(hqDetails)
        navigationToDetailMTLiveData.postValue(Unit)
    }

}