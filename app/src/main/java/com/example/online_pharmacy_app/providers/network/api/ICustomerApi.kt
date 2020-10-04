package com.example.online_pharmacy_app.providers.network.api

import com.example.online_pharmacy_app.viewobjects.Customer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ICustomerApi {
    @FormUrlEncoded
    @POST("customer/register")
    fun registerCustomer(
        @Field("fullName") fullName: String,
        @Field("telephone") telephone: String,
        @Field("email") email: String,
        @Field("token") token: String,
        @Field("dateOfBirth") date: String
    ): Call<Customer>


    @FormUrlEncoded
    @POST("customer/details")
    fun getCustomerDetails(@Field("email") email: String): Call<Customer>
}


