package com.pharmacure.online_pharmacy_app.activities
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.DrugViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_drug_details.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class DrugDetailsActivity : AppCompatActivity(), DIAware, OnCartChangeResultListeners {

    lateinit var drugViewModel: DrugViewModel
    lateinit var cartViewModel: CartViewModal
    override val di: DI by di(this)
    private val factory: ViewModelFactory by instance()
    var selectedDrug: Drug? = null
    var customerID: Int = 0
    private var QUANTITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_details)

        cartViewModel = ViewModelProvider(this, factory).get(CartViewModal::class.java)
        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        drugViewModel.setDrugID(intent.getIntExtra(DRUG_ID, 0))
        customerID = intent.getIntExtra(CUSTOMER_ID, 0)
        drugViewModel.getRemoteDrugByID().observe(this, Observer(::handleDrugResult))


        backButtonImageView.setOnClickListener {
            finish()
        }



        cartViewModel.onCartChangeResultList = this

        addToCartDetailsButton.setOnClickListener {
            if(customerID!=0){
                selectedDrug?.let { drug ->
                    cartViewModel.setAddToCart(
                        QUANTITY,
                        customerID,
                        drug.drugID,
                        drug.unitPrice
                    )
                }

                cartViewModel.addToCart()
            }else{
                this.startLoginActivity()
            }
        }

        viewCartButton.setOnClickListener {
            this.startHomeActivity(OPEN_CART_FRAGS)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun handleDrugResult(result: SResult<Drug>) {
        when (result) {
            is SResult.Loading -> {
                showProgressBar()
                log { "Loading..." }
            }

            is SResult.Success -> {
                selectedDrug = result.data
                hideProgressBar()
                drugNameTextView.text = result.data.drugName
                categoryTextView.text = "${result.data.caTname} \n ${result.data.subName}"
                drugDescriptionTextView.text = result.data.description
                drugUnitPriceTextView.text = result.data.unitPrice.toString()
                Glide.with(drugCoverImageView)
                    .load(result.data.getDrugImageUrl())
                    .into(drugCoverImageView)
            }

            is SResult.Error -> {
                hideProgressBar()
                Toast.makeText(
                    this,
                    "Error occurred ${result.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is SResult.Empty -> {
                showProgressBar()
                showSnackBack("No response from Server")
            }
        }
    }

    private fun hideProgressBar() {
        progressBarDrugDetailsScreen.visibility = View.INVISIBLE
    }

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> {
                showProgressBar()
            }

            is SResult.Success -> {
                hideProgressBar()
                Toast.makeText(
                    this,
                    "Product Added to Cart ${result.data.map { it.quantity }.sum()}",
                    Toast.LENGTH_SHORT
                ).show()

            }

            is SResult.Error -> {
                hideProgressBar()
                Toast.makeText(
                    this,
                    "Error occurred try again!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is SResult.Empty -> showSnackBack("No response from server")
        }
    }


    private fun showSnackBack(message:String){
        Snackbar.make(coordinateLayoutDetails, message, Snackbar.LENGTH_SHORT).apply { show() }
    }

    private fun showProgressBar() {
        progressBarDrugDetailsScreen.visibility = View.VISIBLE
    }



}