package com.example.online_pharmacy_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.online_pharmacy_app.domain.repository.DrugRepository
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.loading
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.coroutines.Dispatchers

class DrugViewModel(private val drugRepository: DrugRepository):ViewModel() {


    private val remoteDrugListLiveData:LiveData<SResult<List<Drug>>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(loading())
            emit(drugRepository.getRemoteDrugs())
        }
    }

    fun getDrugList() = remoteDrugListLiveData

}