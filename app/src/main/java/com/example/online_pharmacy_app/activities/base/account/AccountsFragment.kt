package com.example.online_pharmacy_app.activities.base.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.databinding.FragmentAccountsBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CustomerViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Customer
import kotlinx.android.synthetic.main.item_drug_gridview.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AccountsFragment : Fragment(R.layout.fragment_accounts), KodeinAware {

    private lateinit var binding: FragmentAccountsBinding
    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountsBinding.bind(view)

        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)

        customerViewModel.customerDetails.observe(
            viewLifecycleOwner,
            Observer(::handleCustomerDetails)
        )

    }

    companion object {
        fun newInstance(): AccountsFragment = AccountsFragment()
    }


    private fun handleCustomerDetails(result: SResult<Customer>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> {
                log { result.data.toString() }
                binding.customerNameTextView.text = result.data.fullName
                binding.phoneTextView.text = result.data.telephone
                binding.emailTextView.text = result.data.email

                Glide.with(requireContext()).load(result.data)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)

            }
            is SResult.Error -> startActivity(Intent(context, GoogleSignInActivity::class.java))
            is SResult.Empty -> startActivity(Intent(context, GoogleSignInActivity::class.java))
        }
    }
}