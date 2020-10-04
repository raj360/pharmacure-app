package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.providers.network.api.ICartApi
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.emptyResult
import com.example.online_pharmacy_app.result.errorResult
import com.example.online_pharmacy_app.result.successResult
import com.example.online_pharmacy_app.viewobjects.Cart
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class CartRemoteDataSource(private val cartApi: ICartApi) : ICartRemoteDataSource {
    override suspend fun getCart(customerID: Int): SResult<List<Cart>> =
        when (val result = cartApi.getCartList(customerID).awaitResult()) {
            is Result.Ok -> {
                successResult(result.value)
            }

            is Result.Error -> {
                errorResult(result.response.code, result.exception.message())
            }

            is Result.Exception -> {
                emptyResult()
            }
        }

    override suspend fun addToCart(
        quantity: Int,
        customerID: Int,
        drugID: Int,
        costPrice: Int
    ): SResult<List<Cart>> =
        when (val result =
            cartApi.addToCart(quantity, customerID, drugID, costPrice).awaitResult()) {
            is Result.Ok -> successResult(result.value)
            is Result.Error -> errorResult(result.response.code, result.exception.message())
            is Result.Exception -> {
                log { "This exception occurred => ${result.exception}" }
                emptyResult()
            }
        }

    override suspend fun updateCart(
        customerID: Int,
        quantity: Int,
        cartID: Int
    ): SResult<List<Cart>> =
        when (val result = cartApi.updateCart(customerID, quantity, cartID).awaitResult()) {
            is Result.Ok -> successResult(result.value)
            is Result.Error -> errorResult(result.response.code, result.exception.message())
            is Result.Exception -> emptyResult()
        }

    override suspend fun deleteFromCart(customerID: Int, cartID: Int): SResult<List<Cart>> =
        when (val result = cartApi.deleteFromCart(customerID, cartID).awaitResult()) {
            is Result.Ok -> successResult(result.value)
            is Result.Error -> errorResult(result.response.code, result.exception.message())
            is Result.Exception -> emptyResult()

        }

    override suspend fun clearCart(customerID: Int): SResult<List<Cart>> =
        when (val result = cartApi.clearCart(customerID).awaitResult()) {
            is Result.Ok -> successResult(result.value)
            is Result.Error -> errorResult(result.response.code, result.exception.message())
            is Result.Exception -> emptyResult()

        }

}