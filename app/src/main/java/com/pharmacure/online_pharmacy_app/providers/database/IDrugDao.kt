package com.pharmacure.online_pharmacy_app.providers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacure.online_pharmacy_app.providers.database.dao.base.IBaseDao

import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import com.pharmacure.online_pharmacy_app.viewobjects.DrugFts

@Dao
interface IDrugDao : IBaseDao<Drug> {
    @Query("SELECT * FROM  Drug LIMIT 20")
    fun getAllDrugs(): List<Drug>

    @Query("SELECT * FROM Drug WHERE drugID=:drugID LIMIT 1")
    fun getById(drugID: Int): LiveData<Drug>

    @Query("SELECT * FROM Drug JOIN DrugFts ON Drug.drugID = DrugFts.rowid WHERE DrugFts MATCH :query")
    fun search(query: String): List<Drug>

    @Query("SELECT rowid,* FROM DrugFts")
    fun test():List<DrugFts>

}