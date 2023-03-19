package com.example.hqawesomeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hqawesomeapp.api.ComicService
import com.example.hqawesomeapp.data.ApiCredentials
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.DataState
import com.example.hqawesomeapp.data.Event
import com.example.hqawesomeapp.helper.ApiHelper
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HQViewModel : ViewModel() {

    val hqDetailsLiveData: LiveData<Comic>
        get() = hqDetailsMTLiveData

    private val hqDetailsMTLiveData = MutableLiveData<Comic>()

    val hqListLiveData: LiveData<List<Comic>?>
        get() = _hqListLiveData

    private val _hqListLiveData =
        MutableLiveData<List<Comic>?>()

    val appState: LiveData<DataState>
        get() = _appState
    private val _appState = MutableLiveData<DataState>()

    val navigationToDetailsLiveData
        get() = _navigationToDetailLiveData

    private val _navigationToDetailLiveData = MutableLiveData<Event<Unit>>()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiCredentials.baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val comicService = retrofit.create(ComicService::class.java)

    init {
        _appState.postValue(DataState.Loading)
        getHQData()
    }

    fun onHQSelected(position: Int) {
        val hqDetails = _hqListLiveData.value?.get(position)
        hqDetails.let {
            hqDetailsMTLiveData.postValue(it)
            _navigationToDetailLiveData.postValue(Event(Unit))
        }

    }

    private fun getHQData() {
        val timeStamp = ApiHelper.getCurrentTimeStamp()
        val input = "$timeStamp${ApiCredentials.privateKey}${ApiCredentials.publicKey}"
        val hash = ApiHelper.generateMD5Hash(input)

        viewModelScope.launch {
            val response = comicService.getComicList(timeStamp, ApiCredentials.publicKey, hash, 100)
            if (response.isSuccessful) {
                _hqListLiveData.postValue(response.body()?.data?.results)
                _appState.postValue(DataState.Success)
            } else {
                _appState.postValue(DataState.Error)
            }
        }
    }

}