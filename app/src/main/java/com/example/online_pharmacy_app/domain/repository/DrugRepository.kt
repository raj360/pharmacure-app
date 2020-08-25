package com.example.online_pharmacy_app.domain.repository

import androidx.lifecycle.LiveData
import com.example.online_pharmacy_app.datasource.remote.CustomerRemoteDataSource
import com.example.online_pharmacy_app.datasource.remote.DrugRemoteDataSource
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.mapListTo
import com.example.online_pharmacy_app.viewobjects.Customer
import com.example.online_pharmacy_app.viewobjects.Drug

class DrugRepository(private val drugRemoteDataSource: DrugRemoteDataSource) {

    suspend fun getRemoteDrugs():SResult<List<Drug>> =
        drugRemoteDataSource.getDrugFromInventory().mapListTo()

}