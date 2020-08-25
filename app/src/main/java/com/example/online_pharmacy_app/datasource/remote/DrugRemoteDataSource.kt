package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.providers.network.api.IDrugApi
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.emptyResult
import com.example.online_pharmacy_app.result.errorResult
import com.example.online_pharmacy_app.result.successResult
import com.example.online_pharmacy_app.viewobjects.Drug
import ru.gildor.coroutines.retrofit.ResponseResult
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


}