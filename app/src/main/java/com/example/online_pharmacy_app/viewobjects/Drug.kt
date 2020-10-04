package com.example.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.online_pharmacy_app.common.SERVER_URL
import com.example.online_pharmacy_app.viewobjects.base.IConvertibleTo

@Entity
data class Drug(
    @PrimaryKey
    val drugID: Int,
    val drugName: String,
    val drugType: String,
    val description: String,
    val imageUrl: String,
    val unitPrice: Int,
    val dose: String,
    val categoryID: Int,
    val caTname: String,
    val subCategoryID: Int,
    val subName: String,
    val inventoryID: Int,
    val quantity: Int,
    val adminID: Int
) : IConvertibleTo<Drug> {
    override fun convertTo() =
        Drug(
            this.drugID,
            this.drugName,
            this.drugType,
            this.description,
            this.imageUrl,
            this.unitPrice,
            this.dose,
            this.categoryID,
            this.caTname,
            this.subCategoryID,
            this.subName,
            this.inventoryID,
            this.quantity,
            this.adminID
        )

    fun getDrugImageUrl() = "${SERVER_URL}$imageUrl"
}