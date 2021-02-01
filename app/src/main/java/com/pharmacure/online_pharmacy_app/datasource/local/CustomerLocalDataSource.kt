package com.pharmacure.online_pharmacy_app.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pharmacure.online_pharmacy_app.providers.database.ICustomerDao
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.emptyResult
import com.pharmacure.online_pharmacy_app.result.successResult
import com.pharmacure.online_pharmacy_app.viewobjects.Customer

class CustomerLocalDataSource(private val customerDao: ICustomerDao) : ICustomerLocalDataSource {
    override suspend fun getAll(): LiveData<SResult<Customer>> =
        customerDao.getCustomer().map {
            if (it != null)
                successResult(it)
            else {
                emptyResult()
            }
        }


    override suspend fun insert(data: Customer) {
        customerDao.insert(data)
    }

    override suspend fun signOut() {
        customerDao.signOut()
    }
}