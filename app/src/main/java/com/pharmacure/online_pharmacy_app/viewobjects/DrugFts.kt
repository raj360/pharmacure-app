package com.pharmacure.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4(contentEntity = Drug::class)
data class DrugFts (
   val drugName:String,
   val description:String,
   val caTname:String,
   val subName:String,
)