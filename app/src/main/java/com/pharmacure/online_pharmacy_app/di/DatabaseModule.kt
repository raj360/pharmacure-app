package com.pharmacure.online_pharmacy_app.di

import com.pharmacure.online_pharmacy_app.providers.database.ICustomerDao
import com.pharmacure.online_pharmacy_app.providers.database.IDrugDao
import com.pharmacure.online_pharmacy_app.providers.database.PharmacureDatabase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider


val databaseModule = DI.Module("database_module") {
    bind<ICustomerDao>() with provider { instance<PharmacureDatabase>().getAllCustomerDao() }
    bind<IDrugDao>() with provider { instance<PharmacureDatabase>().getAllDrugDao() }
}