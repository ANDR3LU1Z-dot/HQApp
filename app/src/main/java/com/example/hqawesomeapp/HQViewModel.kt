package com.example.hqawesomeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hqawesomeapp.api.ComicService
import com.example.hqawesomeapp.data.*
import com.example.hqawesomeapp.database.ComicsDatabase
import com.example.hqawesomeapp.helper.ApiHelper
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HQViewModel(application: Application) : AndroidViewModel(application) {

    private val comicsDatabase = ComicsDatabase.getDatabase(application)
    val comicDao = comicsDatabase.comicDao(comicsDatabase)

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
            try {
                val response = comicService.getComicList(timeStamp, ApiCredentials.publicKey, hash, 100)
                if (response.isSuccessful) {
                    val comics = response.body()?.data?.results

                    comics?.let {
                        persistComicData(it)
                    }

                    _hqListLiveData.postValue(comics)
                    _appState.postValue(DataState.Success)
                } else {
                    errorHandling()
                }
            } catch (e: Exception){
                errorHandling()
            }
        }
    }

    private suspend fun errorHandling(){
        val comicList = loadPersistedComicData()

        if(comicList.isNullOrEmpty()){
            _appState.postValue(DataState.Error)
        } else {
            _hqListLiveData.postValue(comicList)
            _appState.postValue(DataState.Success)
        }
    }

    private suspend fun persistComicData(comicList: List<Comic>){
        comicDao.clearComicData()
        comicDao.insertComicList(comicList)
    }

    private suspend fun loadPersistedComicData() = comicDao.getAllComics()?.map {
        mapComicWithPropertiesToComic(it)
    }

    private fun mapComicWithPropertiesToComic(comicWithAllProperties: ComicWithAllProperties): Comic{
        comicWithAllProperties.comic.images = comicWithAllProperties.images
        comicWithAllProperties.comic.textObject = comicWithAllProperties.textObject
        return comicWithAllProperties.comic
    }

}