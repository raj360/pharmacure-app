package com.example.online_pharmacy_app.activities

import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewobjects.Drug

interface OnSearchDrugListener {
    fun searchDrugResult(result: SResult<List<Drug>>)
}