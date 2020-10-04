package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.providers.network.api.IDrugApi
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.emptyResult
import com.example.online_pharmacy_app.result.errorResult
import com.example.online_pharmacy_app.result.successResult
import com.example.online_pharmacy_app.viewobjects.Drug
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class DrugRemoteDataSource(private var drugApi: IDrugApi) : IDrugRemoteDataSource {
    override suspend fun getDrugFromInventory() =
        when (val drugs = drugApi.getDrugsFromInventory().awaitResult()) {
            is Result.Ok -> {
                successResult(drugs.value)
            }
            is Result.Error -> {
                errorResult(drugs.response.code, drugs.exception.message())
            }
            is Result.Exception -> {
                emptyResult()
            }
        }

    override suspend fun getDrugFromInventoryByID(drugID: Int) =
        when (val drugs = drugApi.getDrugsFromInventoryByID(drugID).awaitResult()) {
            is Result.Ok -> {
                successResult(drugs.value)
            }
            is Result.Error -> {
                errorResult(drugs.response.code, drugs.exception.message())
            }
            is Result.Exception -> {
                emptyResult()
            }
        }


    override suspend fun getDrugFromInventoryFromInventory(
        categoryID: Int,
        subCategoryID: Int
    ): SResult<List<Drug>> = when (val drugs =
        drugApi.getDrugsFromInventoryByCategory(categoryID, subCategoryID).awaitResult()) {
        is Result.Ok -> {
            successResult(drugs.value)
        }
        is Result.Error -> {
            errorResult(drugs.response.code, drugs.exception.message())
        }
        is Result.Exception -> {
            emptyResult()
        }
    }

    override suspend fun searchDrug(searchQuery: String): SResult<List<Drug>>  =
        when (val drugs =
            drugApi.searchDrug(searchQuery).awaitResult()) {
            is Result.Ok -> {
                successResult(drugs.value)
            }
            is Result.Error -> {
                errorResult(drugs.response.code, drugs.exception.message())
            }
            is Result.Exception -> {
                emptyResult()
            }
        }

}