package com.pharmacure.online_pharmacy_app.activities

import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.Customer


interface OnCustomerSignInResultListeners {
    fun handleSignInResult(result: SResult<Customer>)
    fun onError(message:String)
}