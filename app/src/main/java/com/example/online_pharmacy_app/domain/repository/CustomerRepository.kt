package com.example.online_pharmacy_app.domain.repository

import android.app.RemoteAction
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.datasource.local.CustomerLocalDataSource
import com.example.online_pharmacy_app.datasource.remote.CustomerRemoteDataSource
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.loading
import com.example.online_pharmacy_app.viewobjects.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomerRepository(
    private val customerLocalDataSource: CustomerLocalDataSource,
    private val customerRemoteDataSource: CustomerRemoteDataSource
) {

    suspend fun getLocalCustomer(): LiveData<SResult<Customer>> = liveData {
              // emit(customerLocalDataSource.getAll()))
        emit(loading())
        emitSource(customerLocalDataSource.getAll())
    }

    suspend fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ): SResult<Customer> =
        withContext(Dispatchers.IO) {
            customerRemoteDataSource.registerCustomer(fullName, telephone, email, token, date)
                .also {
                    when (it) {

                        is SResult.Success -> customerLocalDataSource.insert(it.data)
                    }
                }
        }


}