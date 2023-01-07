package com.example.hqawesomeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.ComicResponse
import com.example.hqawesomeapp.data.DataState
import com.example.hqawesomeapp.hqDetails.HQDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HQViewModel : ViewModel() {

    val hqDetailsLiveData: LiveData<HQDetails>
        get() = hqDetailsMTLiveData

    private val hqDetailsMTLiveData = MutableLiveData<HQDetails>()

    val hqListLiveData: LiveData<List<Comic>?>
        get() = hqListMTLiveData

    private val hqListMTLiveData =
        MutableLiveData<List<Comic>?>()

    val appState: LiveData<DataState>
        get() = _appState
    private val _appState = MutableLiveData<DataState>()

    val navigationToDetailsLiveData
        get() = navigationToDetailMTLiveData

    private val navigationToDetailMTLiveData  = MutableLiveData<Unit>()

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
        val hqDetails = HQDetails("Minha HQ", "Este é apenas um conteudo de texto maior")
        hqDetailsMTLiveData.postValue(hqDetails)
        navigationToDetailMTLiveData.postValue(Unit)
    }

    private fun getHQData(){
        val timeStamp = ApiHelper.getCurrentTimeStamp()
        val input = "$timeStamp${ApiCredentials.privateKey}${ApiCredentials.publicKey}"
        val hash = ApiHelper.generateMD5Hash(input)
        comicService.getComicList(timeStamp, ApiCredentials.publicKey, hash, 100).enqueue(object: Callback<ComicResponse>{
            override fun onResponse(call: Call<ComicResponse>, response: Response<ComicResponse>) {
                if(response.isSuccessful){
                    hqListMTLiveData.postValue(response.body()?.data?.results)
                    _appState.postValue(DataState.Success)
                } else {
                    _appState.postValue(DataState.Error)
                }
            }

            override fun onFailure(call: Call<ComicResponse>, t: Throwable) {
                _appState.postValue(DataState.Error)
            }

        })
    }

}