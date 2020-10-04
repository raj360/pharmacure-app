package com.example.online_pharmacy_app.providers.network.api

import com.example.online_pharmacy_app.viewobjects.Cart
import retrofit2.Call
import retrofit2.http.*

interface ICartApi {

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


//    @FormUrlEncoded
//    @POST("customer/customer-order/{customer_id}/{store_id}")
//    fun makeOrder(
//        @Path("customer_id") customerId: Int,
//        @Field("cart_ids") cartIds: IntArray,
//        @Path("store_id") storeId: Int,
//        @Field("delivery_mode")deliveryMode:String,
//        @Field("location")location:String
//    ): Call<ApiStatus>


}