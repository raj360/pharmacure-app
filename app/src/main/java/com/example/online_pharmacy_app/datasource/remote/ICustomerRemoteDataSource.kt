package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Customer
import retrofit2.http.Field

interface ICustomerRemoteDataSource {
    suspend fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ): SResult<Customer>
}