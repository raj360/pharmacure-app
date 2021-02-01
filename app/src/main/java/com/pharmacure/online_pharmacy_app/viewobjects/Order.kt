package com.pharmacure.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pharmacure.online_pharmacy_app.viewobjects.base.IConvertibleTo

@Entity
data class Order(
    @PrimaryKey
    val paymentID: Int,
    val amount: Double,
    val paymentStatus: String,
    val paymentRef: String,
    val customerID: Int,
    val txRef: String,
    val orderRef: String,
    val charge: String,
    val deliveryMethod: String,
    val itemIDs: List<Int>,
    val created_at: String,
    val updated_at: String?,
    val orderStatus: Int,
    val customerLocation: String
) : IConvertibleTo<Order> {
    override fun convertTo() =
        Order(
            this.paymentID,
            this.amount,
            this.paymentStatus,
            this.paymentRef,
            this.customerID,
            this.txRef,
            this.orderRef,
            this.charge,
            this.deliveryMethod,
            this.itemIDs,
            this.created_at,
            this.updated_at,
            this.orderStatus,
            this.customerLocation
        )

}