package com.pharmacure.online_pharmacy_app.datasource.remote

import com.pharmacure.online_pharmacy_app.providers.network.api.ICustomerApi
import com.pharmacure.online_pharmacy_app.providers.network.api.IPharmacureApi
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.emptyResult
import com.pharmacure.online_pharmacy_app.result.errorResult
import com.pharmacure.online_pharmacy_app.result.successResult
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import okhttp3.MultipartBody
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResponse
import ru.gildor.coroutines.retrofit.awaitResult

class CustomerRemoteDataSource(private val customerApi: IPharmacureApi) : ICustomerRemoteDataSource {

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
    ) =
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

    override suspend fun loginCustomer(email: String, token: String)=
        when (val result =
            customerApi.loginCustomer( email, token).awaitResult()) {
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

    override suspend fun makeOrder(
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
    ) = when (val result = customerApi.makeOrder(
        amount,
        paymentStatus,
        paymentRef,
        customerID,
        txRef,
        orderRef,
        charge,
        deliveryMethod,
        itemIDs,
        location
    ).awaitResult()) {
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

    override suspend fun getOrderDetails(customerID: Int) =
        when (val result = customerApi.getOrderDetails(customerID).awaitResult()) {
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


    override suspend fun getOrderItemDetails(
        customerID: Int,
        paymentID: Int
    ) = when (val result = customerApi.getOrderItemDetails(customerID, paymentID).awaitResult()) {
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

    override suspend fun uploadPrescription(customerID: Int,part: MultipartBody.Part,orderRef: String) =
        when(val result = customerApi.uploadPrescription(customerID,part,orderRef).awaitResult()){
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