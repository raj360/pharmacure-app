package com.example.online_pharmacy_app.di


import com.example.online_pharmacy_app.datasource.remote.CustomerRemoteDataSource
import com.example.online_pharmacy_app.datasource.remote.DrugRemoteDataSource
import com.example.online_pharmacy_app.datasource.remote.ICustomerRemoteDataSource
import com.example.online_pharmacy_app.datasource.remote.IDrugRemoteDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val remoteDataSourceModule = Kodein.Module("remote_data_source_module"){
    bind<ICustomerRemoteDataSource>() with singleton { CustomerRemoteDataSource(instance()) }
    bind() from singleton { CustomerRemoteDataSource(instance()) }

    bind<IDrugRemoteDataSource>() with singleton { DrugRemoteDataSource(instance()) }
    bind() from singleton { DrugRemoteDataSource(instance()) }



}