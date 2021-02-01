package com.pharmacure.online_pharmacy_app.di

import com.pharmacure.online_pharmacy_app.common.SERVER_URL
import com.pharmacure.online_pharmacy_app.providers.network.api.IPharmacureApi
import com.pharmacure.online_pharmacy_app.providers.network.createOkHttpClient
import com.pharmacure.online_pharmacy_app.providers.network.createWebServiceApi
import okhttp3.OkHttpClient
import org.kodein.di.*


private const val TAG_SERVER_URL = ""

val networkModule = DI.Module("network_module") {
    constant(TAG_SERVER_URL) with SERVER_URL
    bind<OkHttpClient>() with singleton { createOkHttpClient() }
    bind<IPharmacureApi>() with singleton { createWebServiceApi(instance(), instance(TAG_SERVER_URL)) }


}