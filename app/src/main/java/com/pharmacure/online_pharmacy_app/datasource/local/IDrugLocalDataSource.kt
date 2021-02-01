package com.pharmacure.online_pharmacy_app.datasource.local

import androidx.lifecycle.LiveData
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.pharmacure.online_pharmacy_app.viewobjects.Drug

interface IDrugLocalDataSource {

    suspend fun getAll(): SResult.Success<List<Drug>>

    suspend fun getById(drugID: Int): LiveData<SResult<Drug>>

    suspend fun insert(data:Drug)

    suspend fun insertAll(data: List<Drug>)

    suspend fun search(query:String):SResult.Success<List<Drug>>


}