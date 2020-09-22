package com.example.online_pharmacy_app.activities

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Drug

interface OnSearchDrugListener {
    fun searchProductResult(result: SResult<List<Drug>>)
}