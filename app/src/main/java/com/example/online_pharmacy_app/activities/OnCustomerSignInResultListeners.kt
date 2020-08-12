package com.example.online_pharmacy_app.activities.auth

import com.example.onlinestorereview.result.SResult
import com.example.onlinestorereview.viewobject.Cart

interface OnCustomerSignInResultListeners {

    fun handleCartResult(result: SResult<List<Cart>>)
}