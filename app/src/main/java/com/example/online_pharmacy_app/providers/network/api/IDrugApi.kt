package com.example.online_pharmacy_app.providers.network.api

import com.example.online_pharmacy_app.viewobjects.Drug
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IDrugApi {

    @GET("drug/get-from-inventory")
    fun getDrugsFromInventory(): Call<List<Drug>>
    @GET("drug/get-from-inventory-by-id/{drugID}")
    fun getDrugsFromInventoryByID(@Path("drugID")drugID:Int): Call<Drug>

    @GET("drug/get-from-inventory-by-category/{categoryID}/{subCategoryID}")
    fun getDrugsFromInventoryByCategory(
        @Path("categoryID") categoryID: Int,
        @Path("subCategoryID") subCategoryID: Int
    ): Call<List<Drug>>


}