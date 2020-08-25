package com.example.online_pharmacy_app.providers.network.api

import com.example.online_pharmacy_app.viewobjects.Drug
import retrofit2.Call
import retrofit2.http.GET

interface IDrugApi {

    @GET("drug/get-from-inventory")
    fun getDrugsFromInventory(): Call<List<Drug>>
}