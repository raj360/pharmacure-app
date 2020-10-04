package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Cart

interface ICartRemoteDataSource {

    suspend fun getCart(customerID: Int): SResult<List<Cart>>

    suspend fun addToCart(
        quantity: Int,
        customerID: Int,
        drugID: Int,
        costPrice: Int
    ): SResult<List<Cart>>

    suspend fun updateCart(
        customerID: Int,
        quantity: Int,
        cartID: Int
    ): SResult<List<Cart>>

    suspend fun deleteFromCart(
        customerID: Int,
        cartID: Int
    ): SResult<List<Cart>>


    suspend fun clearCart(
        customerID: Int
    ): SResult<List<Cart>>


//    suspend fun makeOrder(
//        customerId: Int,
//        cartIds: IntArray,
//        storeId: Int,
//        deliveryMode:String,
//        location:String
//    ): SResult<ApiStatus>
}