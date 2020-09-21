package com.example.online_pharmacy_app.di

import com.example.online_pharmacy_app.common.SERVER_URL
import com.example.online_pharmacy_app.providers.network.api.ICartApi
import com.example.online_pharmacy_app.providers.network.api.ICustomerApi
import com.example.online_pharmacy_app.providers.network.api.IDrugApi
import com.example.online_pharmacy_app.providers.network.createOkHttpClient
import com.example.online_pharmacy_app.providers.network.createWebServiceApi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with

private const val TAG_SERVER_URL = ""

val networkModule = Kodein.Module("network_module") {
    constant(TAG_SERVER_URL) with SERVER_URL
    bind<OkHttpClient>() with singleton { createOkHttpClient() }

    bind<ICustomerApi>() with singleton {
        createWebServiceApi<ICustomerApi>(
            instance(), instance(TAG_SERVER_URL)
        )

    }
    bind<IDrugApi>() with singleton {
        createWebServiceApi<IDrugApi>(
            instance(), instance(TAG_SERVER_URL)
        )
    }

    bind<ICartApi>() with singleton {
        createWebServiceApi<ICartApi>(
            instance(),
            instance(TAG_SERVER_URL)
        )
    }


}