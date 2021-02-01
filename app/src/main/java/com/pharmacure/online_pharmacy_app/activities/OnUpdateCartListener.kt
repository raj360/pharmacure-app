package com.pharmacure.online_pharmacy_app.activities

interface OnUpdateCartListener {

    fun updateHotCount(count: Int = 0)
    fun showCartProgress()
    fun hideCartProgress()
}