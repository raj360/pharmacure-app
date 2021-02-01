package com.pharmacure.online_pharmacy_app.datasource.remote

import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.Cart

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

}