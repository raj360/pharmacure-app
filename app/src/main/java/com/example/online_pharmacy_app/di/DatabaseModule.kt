package com.example.online_pharmacy_app.di

import com.example.online_pharmacy_app.providers.database.ICustomerDao
import com.example.online_pharmacy_app.providers.database.PharmacureDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val databaseModule = Kodein.Module("database_module") {
    bind<ICustomerDao>() with provider { instance<PharmacureDatabase>().getAllCustomerDao() }
}