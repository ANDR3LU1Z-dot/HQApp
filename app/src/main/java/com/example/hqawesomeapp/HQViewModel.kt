package com.example.hqawesomeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hqawesomeapp.data.Comic
import com.example.hqawesomeapp.data.DataState
import com.example.hqawesomeapp.data.Event
import com.example.hqawesomeapp.repository.HqRepository
import kotlinx.coroutines.launch

class HQViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        const val TAG = "HQViewModel"
    }

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

    private val hqRepository = HqRepository(application)

    init {
        _appState.postValue(DataState.Loading)
        getHqData()
    }

    fun onHQSelected(position: Int) {
        val hqDetails = _hqListLiveData.value?.get(position)
        hqDetails.let {
            hqDetailsMTLiveData.postValue(it)
            _navigationToDetailLiveData.postValue(Event(Unit))
        }

    }

    private fun getHqData(){
        _appState.postValue(DataState.Loading)

        viewModelScope.launch {
            val comicListResult = hqRepository.getHqData()
            Log.d(TAG, "comic result list: $comicListResult")

            comicListResult.fold(
                onSuccess = {
                    _hqListLiveData.value = it
                    _appState.value = DataState.Success
                },
                onFailure = {
                    Log.d(TAG, "Falha")
                    _appState.value = DataState.Error
                }
            )

        }
    }

}