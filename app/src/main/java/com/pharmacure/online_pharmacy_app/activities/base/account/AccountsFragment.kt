package com.pharmacure.online_pharmacy_app.activities.base.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.pharmacure.online_pharmacy_app.common.FRAG_TO_OPEN
import com.pharmacure.online_pharmacy_app.common.startHomeActivity
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class AccountsFragment : Fragment(R.layout.fragment_accounts), DIAware {

    override val di: DI by di()
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var auth: FirebaseAuth
    var customer: Customer? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        },200)

        MobileAds.initialize(requireContext()) {}

        val adRequest = AdRequest.Builder().build()
        adViewBanner.visibility = View.GONE
        adViewBanner.loadAd(adRequest)
        loadCustomerData(customer)
        adViewBanner.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adViewBanner.visibility = View.VISIBLE
            }
        }


        signOutTextView.setOnClickListener {
            customerViewModel.signOut().also {
                auth.signOut()
                requireContext().startHomeActivity(FRAG_TO_OPEN)
            }
        }

    }


    private fun loadCustomerData(customer: Customer?) {

        if (customer == null) {
            customerNameTextView.text = getString(R.string.no_acount_not)
            phoneTextView.visibility = View.GONE
            emailTextView.visibility = View.GONE
            editProfileTextView.visibility = View.INVISIBLE
            signOutTextView.visibility = View.INVISIBLE
            startActivity(Intent(context, GoogleSignInActivity::class.java))
        } else {
            customerNameTextView.text = customer.fullName
            phoneTextView.text = customer.telephone
            emailTextView.text = customer.email


        }

    }



    companion object {
        @JvmStatic
        fun getInstance() = AccountsFragment()

    }


}