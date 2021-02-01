package com.pharmacure.online_pharmacy_app.activities.categories.ui.main

import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewobjects.Drug

interface OnDrugByCategoryResult {

    fun handleDrugsByCategoryResult(result: SResult<List<Drug>>)
}