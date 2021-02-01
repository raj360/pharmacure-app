package com.pharmacure.online_pharmacy_app.viewobjects

import com.pharmacure.online_pharmacy_app.viewobjects.base.IConvertibleTo

data class ApiResponse(
    val error: Boolean,
    val message: String
) : IConvertibleTo<ApiResponse> {
    override fun convertTo() = ApiResponse(
        this.error,
        this.message
    )
}