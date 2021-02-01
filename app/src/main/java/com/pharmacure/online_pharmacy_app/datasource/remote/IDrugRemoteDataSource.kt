package com.pharmacure.online_pharmacy_app.datasource.remote

import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.Drug


interface IDrugRemoteDataSource {

    suspend fun getDrugFromInventory(): SResult<List<Drug>>
    suspend fun getDrugFromInventoryByID(drugID: Int): SResult<Drug>
    suspend fun getDrugFromInventoryFromInventory(
        categoryID: Int,
        subCategoryID: Int
    ): SResult<List<Drug>>

    suspend fun searchDrug(searchQuery: String): SResult<List<Drug>>

}