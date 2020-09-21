package com.example.online_pharmacy_app.activities.categories.ui.main

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Drug

interface OnDrugByCategoryResult {

    fun handleDrugsByCategoryResult(result: SResult<List<Drug>>)
}