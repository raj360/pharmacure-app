package com.example.online_pharmacy_app.viewobjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.online_pharmacy_app.viewobjects.base.IConvertibleTo

@Entity
data class Customer(
    @PrimaryKey
    val customerID: Int,
    val fullName: String,
    val telephone: String,
    val email: String,
    val token: String?,
    val dateOfBirth: String?
) : IConvertibleTo<Customer> {
    override fun convertTo() =
        Customer(
            this.customerID,
            this.fullName,
            this.telephone,
            this.email,
            this.token,
            this.dateOfBirth
        )


}