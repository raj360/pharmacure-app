package com.example.online_pharmacy_app.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.common.DRUG_ID
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Drug
import kotlinx.android.synthetic.main.activity_drug_details.*
import kotlinx.android.synthetic.main.item_drug_gridview.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class DrugDetailsActivity : AppCompatActivity(), KodeinAware {

    lateinit var drugViewModel: DrugViewModel
    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_details)

        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        drugViewModel.setDrugID(intent.getIntExtra(DRUG_ID, 0))
        drugViewModel.getRemoteDrugByID().observe(this, Observer(::handleDrugResult))

    }


    @SuppressLint("SetTextI18n")
    private fun handleDrugResult(result: SResult<Drug>) {
        when (result) {
            is SResult.Loading ->{
                progressBarDrugDetailsScreen.visibility = View.VISIBLE
                log { "Loading..." }
            }
            is SResult.Success -> {
                progressBarDrugDetailsScreen.visibility = View.INVISIBLE
                drugNameTextView.text = result.data.drugName
                categoryTextView.text = "${result.data.caTname} \n ${result.data.subName}"
                drugDescriptionTextView.text = result.data.description
                drugUnitPriceTextView.text = result.data.unitPrice.toString()
                Glide.with(drugCoverImageView)
                    .load(result.data.getDrugImageUrl())
                    .into(drugCoverImageView)

            }
            is SResult.Error -> Toast.makeText(this,"Error occurred ${result.message}",Toast.LENGTH_SHORT).show()
            is SResult.Empty -> progressBarDrugDetailsScreen.visibility=View.VISIBLE
        }
    }


}