package com.example.online_pharmacy_app.activities

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Customer


interface OnCustomerSignInResultListeners {
    fun handleSignInResult(result: SResult<Customer>)
}