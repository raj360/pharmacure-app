package com.example.online_pharmacy_app.di


import com.example.online_pharmacy_app.datasource.local.CustomerLocalDataSource
import com.example.online_pharmacy_app.datasource.local.ICustomerLocalDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val localDataSourceModule = Kodein.Module("local_data_source") {
    bind<ICustomerLocalDataSource>() with singleton { CustomerLocalDataSource(instance()) }
    bind() from singleton { CustomerLocalDataSource(instance()) }


}