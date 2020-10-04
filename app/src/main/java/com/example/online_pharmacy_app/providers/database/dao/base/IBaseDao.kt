package com.example.online_pharmacy_app.providers.database.dao.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE


interface IBaseDao<T> {

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<T>)

    @Insert(onConflict = REPLACE)
    fun insert(data: T)

    @Delete
    fun delete(data: T)


}