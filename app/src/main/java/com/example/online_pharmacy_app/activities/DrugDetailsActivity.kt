package com.example.online_pharmacy_app.activities
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.common.*
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Cart
import com.example.online_pharmacy_app.viewobjects.Drug
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_drug_details.*
import kotlinx.android.synthetic.main.fragment_category_switcher.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class DrugDetailsActivity : AppCompatActivity(), KodeinAware, OnCartChangeResultListeners {

    lateinit var drugViewModel: DrugViewModel
    lateinit var cartViewModel: CartViewModal
    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    var selectedDrug: Drug? = null
    var customerID: Int = 0
    var QUANTITY = 1

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
            selectedDrug?.let { drug ->
                cartViewModel.setAddToCart(
                    QUANTITY,
                    customerID,
                    drug.drugID,
                    drug.unitPrice
                )
            }

            cartViewModel.addToCart()
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


    fun showSnackBack(message:String){
        Snackbar.make(coordinateLayoutDetails, message, Snackbar.LENGTH_SHORT).apply { show() }
    }

    private fun showProgressBar() {
        progressBarDrugDetailsScreen.visibility = View.VISIBLE
    }


}