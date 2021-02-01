package com.pharmacure.online_pharmacy_app.domain.repository

import androidx.lifecycle.liveData
import com.pharmacure.online_pharmacy_app.common.log
import com.pharmacure.online_pharmacy_app.datasource.local.DrugLocalDataSource
import com.pharmacure.online_pharmacy_app.datasource.remote.DrugRemoteDataSource
import com.pharmacure.online_pharmacy_app.result.*
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugRepository(private val drugRemoteDataSource: DrugRemoteDataSource,
                     private val drugLocalDataSource: DrugLocalDataSource) {



    suspend fun getLocalDrugs()  = liveData {
        emit(loading())
        emit(drugLocalDataSource.getAll().apply {
            log { "Checkinf if we have cached drugs" }
            log { "${this.data}" }
            if (this.data.isNotEmpty()) this.data
            else
                emptyResult()
        })
    }

    suspend fun getLocalDrug(drugID: Int) = drugLocalDataSource.getById(drugID)

    suspend fun getRemoteDrugs() = drugRemoteDataSource.getDrugFromInventory().also {
        log { "saving after fetching" }
       if(it is SResult.Success && it.data.isNotEmpty()) drugLocalDataSource.insertAll(it.data)
    }.mapListTo()

    suspend fun getRemoteDrugsByID(drugID: Int): SResult<Drug> =
        drugRemoteDataSource.getDrugFromInventoryByID(drugID).mapTo()

    suspend fun getRemoteDrugsByCategory(categoryID: Int, subCategoryID: Int)=
        withContext(Dispatchers.IO) {
            drugRemoteDataSource.getDrugFromInventoryFromInventory(categoryID, subCategoryID)
                .mapListTo()
        }.also {
            log { "testing the drugFts" }
           var result =   drugLocalDataSource.search("")
            log { "${result.data}" }
        }

    suspend fun searchDrug(searchQuery:String) =
        withContext(Dispatchers.IO){
            drugLocalDataSource.search(searchQuery).mapListTo()
        }

}