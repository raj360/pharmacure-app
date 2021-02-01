package com.pharmacure.online_pharmacy_app.providers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import com.pharmacure.online_pharmacy_app.viewobjects.DrugFts

//this is the setup of our local database
@Database(entities = [Customer::class,Drug::class,DrugFts::class], version = 3, exportSchema = false)
abstract class PharmacureDatabase : RoomDatabase() {
    abstract fun getAllCustomerDao(): ICustomerDao
    abstract fun getAllDrugDao(): IDrugDao
}

