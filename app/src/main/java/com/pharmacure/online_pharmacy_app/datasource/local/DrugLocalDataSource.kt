package com.pharmacure.online_pharmacy_app.datasource.local


import androidx.lifecycle.map
import com.pharmacure.online_pharmacy_app.providers.database.IDrugDao
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.emptyResult
import com.pharmacure.online_pharmacy_app.result.successResult
import com.pharmacure.online_pharmacy_app.viewobjects.Drug

class DrugLocalDataSource(private val drugDao: IDrugDao) : IDrugLocalDataSource {
    override suspend fun getAll(): SResult.Success<List<Drug>>  = successResult(drugDao.getAllDrugs())

    override suspend fun getById(drugID: Int)= drugDao.getById(drugID).map { localDrug ->
            if (localDrug !=null) successResult(localDrug)
            else emptyResult()
        }

    override suspend fun insert(data: Drug) =  drugDao.insert(data)

    override suspend fun insertAll(data: List<Drug>) = drugDao.insertAll(data)

    override suspend fun search(query: String): SResult.Success<List<Drug>>  = successResult(drugDao.search("%$query%"))

    fun test() = drugDao.test()

}