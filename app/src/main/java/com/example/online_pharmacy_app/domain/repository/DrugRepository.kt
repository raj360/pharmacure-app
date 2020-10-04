package com.example.online_pharmacy_app.domain.repository

import com.example.online_pharmacy_app.datasource.remote.DrugRemoteDataSource
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.mapListTo
import com.example.online_pharmacy_app.result.mapTo
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrugRepository(private val drugRemoteDataSource: DrugRemoteDataSource) {

    suspend fun getRemoteDrugs(): SResult<List<Drug>> =
        drugRemoteDataSource.getDrugFromInventory().mapListTo()

    suspend fun getRemoteDrugsByID(drugID: Int): SResult<Drug> =
        drugRemoteDataSource.getDrugFromInventoryByID(drugID).mapTo()

    suspend fun getRemoteDrugsByCategory(categoryID: Int, subCategoryID: Int): SResult<List<Drug>> =
        withContext(Dispatchers.IO) {
            drugRemoteDataSource.getDrugFromInventoryFromInventory(categoryID, subCategoryID)
                .mapListTo()
        }

    suspend fun searchDrug(searchQuery:String) =
        withContext(Dispatchers.IO){
            drugRemoteDataSource.searchDrug(searchQuery).mapListTo()
        }


}