package com.pharmacure.online_pharmacy_app.datasource.remote

import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.ApiResponse
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.pharmacure.online_pharmacy_app.viewobjects.Order
import com.pharmacure.online_pharmacy_app.viewobjects.OrderItem
import okhttp3.MultipartBody

interface ICustomerRemoteDataSource {

    suspend fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ): SResult<Customer>


    suspend fun loginCustomer(
        email: String,
        token: String,

    ): SResult<Customer>

    suspend fun customerDetails(
        email: String
    ): SResult<Customer>


    suspend fun makeOrder(
        amount: Double,
        paymentStatus: String,
        paymentRef: String,
        customerID: Int,
        txRef: String,
        orderRef: String,
        charge: Double,
        deliveryMethod: String,
        itemIDs: IntArray,
        location: String
    ): SResult<ApiResponse>

    suspend fun getOrderDetails(
        customerID: Int
    ): SResult<List<Order>>

    suspend fun getOrderItemDetails(
        customerID: Int,
        paymentID: Int
    ): SResult<List<OrderItem>>

    suspend fun uploadPrescription(customerID: Int,part: MultipartBody.Part,orderRef: String):SResult<ApiResponse>
}