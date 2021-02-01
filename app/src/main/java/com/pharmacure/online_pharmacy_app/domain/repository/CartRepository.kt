package com.pharmacure.online_pharmacy_app.domain.repository

import com.pharmacure.online_pharmacy_app.datasource.remote.CartRemoteDataSource
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.loading
import com.pharmacure.online_pharmacy_app.result.mapListTo
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val cartRemoteDataSource: CartRemoteDataSource) {

    suspend fun getRemoteCartList(customerID: Int): SResult<List<Cart>> =
        withContext(Dispatchers.IO) {
            loading()
            cartRemoteDataSource.getCart(customerID)
        }.mapListTo()

    suspend fun addToCart(
        quantity: Int,
        customerID: Int,
        drugID: Int,
        costPrice: Int
    ): SResult<List<Cart>> =
        withContext(Dispatchers.IO) {
            loading()
            cartRemoteDataSource.addToCart(
                quantity,
                customerID,
                drugID,
                costPrice
            ).mapListTo()

        }

    suspend fun updateCart(
        customerID: Int,
        quantity: Int,
        cartID: Int
    ): SResult<List<Cart>> = withContext(Dispatchers.IO) {
        loading()
        cartRemoteDataSource.updateCart(customerID, quantity, cartID).mapListTo()
    }

    suspend fun deleteFromCart(customerID: Int, cartID: Int): SResult<List<Cart>> =
        withContext(Dispatchers.IO) {
            loading()
            cartRemoteDataSource.deleteFromCart(customerID, cartID).mapListTo().mapListTo()
        }

    suspend fun clearCart(customerID: Int): SResult<List<Cart>> =
        withContext(Dispatchers.IO) {
            loading()
            cartRemoteDataSource.clearCart(customerID).mapListTo().mapListTo()

        }





}