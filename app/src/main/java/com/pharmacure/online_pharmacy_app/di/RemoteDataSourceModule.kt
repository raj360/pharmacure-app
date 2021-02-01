package com.pharmacure.online_pharmacy_app.di


import com.pharmacure.online_pharmacy_app.datasource.remote.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val remoteDataSourceModule = DI.Module("remote_data_source_module") {

    bind() from singleton { CustomerRemoteDataSource(instance()) }
    bind() from singleton { DrugRemoteDataSource(instance()) }
   bind() from singleton { CartRemoteDataSource(instance()) }
}