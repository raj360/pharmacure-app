package com.pharmacure.online_pharmacy_app.providers.network.api

import com.pharmacure.online_pharmacy_app.viewobjects.ApiResponse
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.pharmacure.online_pharmacy_app.viewobjects.Order
import com.pharmacure.online_pharmacy_app.viewobjects.OrderItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("customer/orders/{customerID}")
    fun makeOrder(
        @Field("amount") amount: Double,
        @Field("paymentStatus") paymentStatus: String,
        @Field("paymentRef") paymentRef: String,
        @Path("customerID") customerID: Int,
        @Field("txRef") txRef: String,
        @Field("orderRef") orderRef: String,
        @Field("charge") charge: Double,
        @Field("deliveryMethod") deliveryMethod: String,
        @Field("itemIDs") itemIDs:IntArray,
        @Field("location") location: String
    ): Call<ApiResponse>


    @GET("customer/orders/{customerID}")
    fun getOrderDetails(
        @Path("customerID") customerID: Int
    ): Call<List<Order>>

    @GET("/customer/order-details/{customerID}/{paymentID}")
    fun getOrderItemDetails(
        @Path("customerID") customerID: Int,
        @Path("paymentID") paymentID: Int
    ):Call<List<OrderItem>>


    @Multipart
    @POST("/customer/order-by-prescription/{customerID}/{orderRef}")
    fun uploadPrescription(
        @Path("customerID") customerID: Int,
        @Part part: MultipartBody.Part,
        @Path("orderRef")orderRef:String
    ): Call<ApiResponse>

}


