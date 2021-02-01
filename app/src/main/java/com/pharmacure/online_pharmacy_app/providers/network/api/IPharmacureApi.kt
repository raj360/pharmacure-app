package com.pharmacure.online_pharmacy_app.providers.network.api

import com.pharmacure.online_pharmacy_app.viewobjects.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IPharmacureApi {

    //-----Cart Routes
    @GET("drug/get-from-cart/{customerID}")
    fun getCartList(@Path("customerID") customerID: Int): Call<List<Cart>>


    @FormUrlEncoded
    @POST("drug/add-to-cart/{drugID}/{customerID}")
    fun addToCart(
        @Field("quantity") quantity: Int,
        @Path("customerID") customerId: Int,
        @Path("drugID") drugID: Int,
        @Field("costPrice") costPrice: Int
    ): Call<List<Cart>>
    
    @FormUrlEncoded
    @POST("drug/update-cart/{cartID}/{customerID}")
    fun updateCart(
        @Path("customerID") customerID: Int,
        @Field("quantity") quantity: Int,
        @Path("cartID") cartID: Int
    ): Call<List<Cart>>


    @GET("drug/delete-one-from-cart/{cartID}/{customerID}")
    fun deleteFromCart(
        @Path("customerID") customerID: Int,
        @Path("cartID") cartID: Int
    ): Call<List<Cart>>



    @GET("drug/delete-one-from-cart/{cartID}/{customerID}")
    fun clearCart(
        @Path("customerID") customerID: Int
    ): Call<List<Cart>>


    //-----Customer Routes
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
    @POST("customer/login")
    fun loginCustomer(
        @Field("email") email: String,
        @Field("token") token: String,
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


    //-----Drug routes
    @GET("drug/get-from-inventory")
    fun getDrugsFromInventory(): Call<List<Drug>>

    @GET("drug/get-from-inventory-by-id/{drugID}")
    fun getDrugsFromInventoryByID(@Path("drugID") drugID: Int): Call<Drug>

    @GET("drug/get-from-inventory-by-category/{categoryID}/{subCategoryID}")
    fun getDrugsFromInventoryByCategory(
        @Path("categoryID") categoryID: Int,
        @Path("subCategoryID") subCategoryID: Int
    ): Call<List<Drug>>

    @GET("drug/{searchQuery}")
    fun searchDrug(
        @Path("searchQuery") searchQuery:String
    ):Call<List<Drug>>

}
