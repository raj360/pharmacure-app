package com.example.online_pharmacy_app.viewobjects

data class Customer(
    val customerID: Int,
    val fullName: String,
    val telephone: String,
    val address: String,
    val email: String,
    val token: String,
    val dateOfBirth: String
)