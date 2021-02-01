package com.pharmacure.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pharmacure.online_pharmacy_app.common.SERVER_URL
import com.pharmacure.online_pharmacy_app.viewobjects.base.IConvertibleTo

@Entity
data class OrderItem(
    @PrimaryKey
    val drugID: Int,
    val drugName: String,
    val imageUrl: String,
    val customerID: Int,
    val cartID: Int,
    val costPrice: Int,
    val quantity: Int,
    val created: String,
    val last_updated: String?
) : IConvertibleTo<OrderItem> {
    override fun convertTo() =
        OrderItem(
            this.drugID,
            this.drugName,
            this.imageUrl,
            this.customerID,
            this.cartID,
            this.costPrice,
            this.quantity,
            this.created,
            this.last_updated
        )

    fun getDrugImageUrl() = "${SERVER_URL}$imageUrl"
}