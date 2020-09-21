package com.example.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.online_pharmacy_app.common.SERVER_URL
import com.example.online_pharmacy_app.viewobjects.base.IConvertibleTo

@Entity
data class Cart(
    @PrimaryKey
    val drugID: Int,
    val drugName: String,
    val drugType: String,
    val description: String,
    val imageUrl: String,
    val unitPrice: Int,
    val dose: String,
    val categoryID: Int,
    val cartID: Int,
    val caTname: String,
    val subCategoryID: Int,
    val subName: String,
    val customerID: Int,
    val costPrice: Int,
    val quantity: Int,
    val status: Int,
    val created: String,
    val last_updated: String?
) : IConvertibleTo<Cart> {
    override fun convertTo() =
        Cart(
            this.drugID,
            this.drugName,
            this.drugType,
            this.description,
            this.imageUrl,
            this.unitPrice,
            this.dose,
            this.categoryID,
            this.cartID,
            this.caTname,
            this.subCategoryID,
            this.subName,
            this.customerID,
            this.costPrice,
            this.quantity,
            this.status,
            this.created,
            this.last_updated
        )

    fun getImageUrlString() = "${SERVER_URL}$imageUrl"
}