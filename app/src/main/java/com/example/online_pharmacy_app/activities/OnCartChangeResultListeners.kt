package com.example.online_pharmacy_app.activities

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Cart

interface OnCartChangeResultListeners {

    fun handleCartResult(result: SResult<List<Cart>>)
}