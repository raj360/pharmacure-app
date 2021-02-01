package com.pharmacure.online_pharmacy_app.viewmodels

import androidx.lifecycle.*
import com.pharmacure.online_pharmacy_app.activities.OnSearchDrugListener
import com.pharmacure.online_pharmacy_app.activities.categories.ui.main.OnDrugByCategoryResult
import com.pharmacure.online_pharmacy_app.common.log
import com.pharmacure.online_pharmacy_app.domain.repository.DrugRepository
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.loading
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DrugViewModel(private val drugRepository: DrugRepository) : ViewModel() {


    private var _drugID: MutableLiveData<Int> = MutableLiveData()
    var onDrugsByCategoryResult: OnDrugByCategoryResult? = null
    var onSearchDrugListener: OnSearchDrugListener? = null




    private val remoteDrugLiveData: LiveData<SResult<Drug>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emitSource(drugRepository.getLocalDrug(_drugID.value!!))
            emit(drugRepository.getRemoteDrugsByID(_drugID.value!!))
        }
    }

    private val remoteDrugsLiveData: LiveData<SResult<List<Drug>>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(loading())
            emitSource(drugRepository.getLocalDrugs())
            emit(drugRepository.getRemoteDrugs())
        }
    }

     fun getDrugList() =  remoteDrugsLiveData

    fun getDrugsByCategoryList(categoryID: Int, subCategoryID: Int) =
        viewModelScope.launch {
            loading()
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
            drugRepository.searchDrug(sanitizeSearchQuery(searchQuery)).let {
                loading()
                onSearchDrugListener?.searchDrugResult(it)
               if(it is SResult.Success)
                   log { "On search result ${it.data}" }
            }
        }


    private fun sanitizeSearchQuery(query: String): String {
        return "\"$query\""
    }

}