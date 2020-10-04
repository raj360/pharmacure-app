package com.example.online_pharmacy_app.datasource.remote

import com.example.online_pharmacy_app.providers.network.api.ICustomerApi
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.emptyResult
import com.example.online_pharmacy_app.result.errorResult
import com.example.online_pharmacy_app.result.successResult
import com.example.online_pharmacy_app.viewobjects.Customer
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class CustomerRemoteDataSource(private val customerApi: ICustomerApi) : ICustomerRemoteDataSource {

    /**
     * use of the SResult and kotlin awaitResult to easily get the
     * necessary texts
     */
    override suspend fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ): SResult<Customer> =
        when (val result =
            customerApi.registerCustomer(fullName, telephone, email, token, date).awaitResult()) {
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


    override suspend fun customerDetails(email: String) =
        when (val result = customerApi.getCustomerDetails(email).awaitResult()) {
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


}