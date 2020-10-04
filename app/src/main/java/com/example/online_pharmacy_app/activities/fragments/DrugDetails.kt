package com.example.online_pharmacy_app.activities.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Drug
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_category_switcher.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class DrugDetails : Fragment(R.layout.fragment_drug_details), KodeinAware {

    lateinit var drugViewModel: DrugViewModel
    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    var drugID: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        drugViewModel.setDrugID(drugID)
        drugViewModel.getRemoteDrugByID().observe(viewLifecycleOwner, Observer(::handleDrugResult))

    }


    private fun handleDrugResult(result: SResult<Drug>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> {
                log { "Result ${result.data}" }
            }
            is SResult.Error -> log { "Some error occurred" }
            is SResult.Empty -> log { "Empty result" }
        }
    }



}