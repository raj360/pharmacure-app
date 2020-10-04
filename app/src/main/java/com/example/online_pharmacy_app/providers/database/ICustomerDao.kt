package com.example.online_pharmacy_app.providers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.online_pharmacy_app.providers.database.dao.base.IBaseDao
import com.example.online_pharmacy_app.viewobjects.Customer

@Dao
interface ICustomerDao : IBaseDao<Customer> {
    @Query("SELECT * FROM Customer LIMIT 1")
    fun getCustomer(): LiveData<Customer>

    @Query("DELETE FROM Customer")
    fun signOut()
}