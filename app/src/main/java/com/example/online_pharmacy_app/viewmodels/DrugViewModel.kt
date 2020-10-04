package com.example.online_pharmacy_app.viewmodels

import androidx.lifecycle.*
import com.example.online_pharmacy_app.activities.OnSearchDrugListener
import com.example.online_pharmacy_app.activities.categories.ui.main.OnDrugByCategoryResult
import com.example.online_pharmacy_app.domain.repository.DrugRepository
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.loading
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrugViewModel(private val drugRepository: DrugRepository) : ViewModel() {


    private var _drugID: MutableLiveData<Int> = MutableLiveData()
    var onDrugsByCategoryResult: OnDrugByCategoryResult? = null
    var onSearchDrugListener: OnSearchDrugListener? = null

    private val remoteDrugListLiveData: LiveData<SResult<List<Drug>>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emit(drugRepository.getRemoteDrugs())
        }
    }


    private val remoteDrugLiveData: LiveData<SResult<Drug>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emit(drugRepository.getRemoteDrugsByID(_drugID.value!!))
        }
    }


    fun getDrugList() = remoteDrugListLiveData

    fun getDrugsByCategoryList(categoryID: Int, subCategoryID: Int) =
        viewModelScope.launch {
            drugRepository.getRemoteDrugsByCategory(categoryID, subCategoryID).also {
                onDrugsByCategoryResult?.handleDrugsByCategoryResult(it)
            }
        }

    fun setDrugID(drugID: Int) {
        _drugID.value = drugID
    }

    fun getRemoteDrugByID() = remoteDrugLiveData


    fun searchDrug(searchQuery:String) =
        viewModelScope.launch {
            drugRepository.searchDrug(searchQuery).let {
                loading()
                onSearchDrugListener?.searchDrugResult(it)
            }
        }

}