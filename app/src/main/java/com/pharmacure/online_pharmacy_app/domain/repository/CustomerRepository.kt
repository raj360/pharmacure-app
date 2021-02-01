package com.pharmacure.online_pharmacy_app.domain.repository

import androidx.lifecycle.liveData
import com.pharmacure.online_pharmacy_app.datasource.local.CustomerLocalDataSource
import com.pharmacure.online_pharmacy_app.datasource.remote.CustomerRemoteDataSource
import com.pharmacure.online_pharmacy_app.result.*
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody


class CustomerRepository(
    private val customerLocalDataSource: CustomerLocalDataSource,
    private val customerRemoteDataSource: CustomerRemoteDataSource
) {

    suspend fun getLocalCustomer() = liveData {
        emit(loading())
        emitSource(customerLocalDataSource.getAll())
    }

    suspend fun loginCustomer(email: String,token: String) =
        withContext(Dispatchers.IO) {
            loading()
            customerRemoteDataSource.loginCustomer(email, token)
                .also {
                    when (it) {
                        is SResult.Success -> if(it.data.email.isNotEmpty()){
                            customerLocalDataSource.insert(it.data)
                        }
                    }
                }
        }

    suspend fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ): SResult<Customer> =
        withContext(Dispatchers.IO) {
            loading()
            customerRemoteDataSource.registerCustomer(fullName, telephone, email, token, date)
        }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            loading()
            customerLocalDataSource.signOut().also {
                return@also
            }
        }
    }

    suspend fun getCustomerDetails(email: String): SResult<Customer> =
        withContext(Dispatchers.IO) {
            loading()
            customerRemoteDataSource.customerDetails(email).also {
                when (it) {
                    is SResult.Success -> it.data.also { customer ->
                        if (customer.email.isNotEmpty()) customerLocalDataSource.insert(customer)
                    }
                }
            }
        }

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
    ) = withContext(Dispatchers.IO) {
        loading()
        customerRemoteDataSource.makeOrder(
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
        ).mapTo()

    }

    suspend fun getOrderDetails(customerID: Int) =
        liveData {
            emit(loading())
            emit(customerRemoteDataSource.getOrderDetails(customerID).mapListTo())
        }

    suspend fun getOrderItemDetails(
        customerID: Int,
        paymentID: Int
    ) = liveData {
        emit(loading())
        emit(customerRemoteDataSource.getOrderItemDetails(customerID, paymentID).mapListTo())
    }

    suspend fun uploadPrescription(customerID: Int,part: MultipartBody.Part,orderRef: String)  =
        liveData {
            emit(loading())
            emit(customerRemoteDataSource.uploadPrescription(customerID,part,orderRef).mapTo())
        }




}