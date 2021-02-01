package com.pharmacure.online_pharmacy_app.di


import com.pharmacure.online_pharmacy_app.datasource.local.CustomerLocalDataSource
import com.pharmacure.online_pharmacy_app.datasource.local.DrugLocalDataSource
import com.pharmacure.online_pharmacy_app.datasource.local.ICustomerLocalDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


val localDataSourceModule = DI.Module("local_data_source") {
    bind() from singleton { CustomerLocalDataSource(instance()) }
    bind() from singleton {DrugLocalDataSource(instance())}

}