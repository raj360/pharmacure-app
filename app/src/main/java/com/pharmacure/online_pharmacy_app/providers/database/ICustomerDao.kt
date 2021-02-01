package com.pharmacure.online_pharmacy_app.providers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacure.online_pharmacy_app.providers.database.dao.base.IBaseDao
import com.pharmacure.online_pharmacy_app.viewobjects.Customer

@Dao
interface ICustomerDao : IBaseDao<Customer> {
    @Query("SELECT * FROM Customer LIMIT 1")
    fun getCustomer(): LiveData<Customer>

    @Query("DELETE FROM Customer")
    fun signOut()
}