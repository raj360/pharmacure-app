package com.example.online_pharmacy_app.providers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.online_pharmacy_app.viewobjects.Customer

//this is the setup of our local database
@Database(entities = [Customer::class], version = 1, exportSchema = false)
abstract class PharmacureDatabase : RoomDatabase() {
    abstract fun getAllCustomerDao(): ICustomerDao
}