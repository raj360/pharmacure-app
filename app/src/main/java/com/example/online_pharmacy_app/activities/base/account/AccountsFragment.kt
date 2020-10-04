package com.example.online_pharmacy_app.activities.base.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.example.online_pharmacy_app.common.FRAG_TO_OPEN
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.common.startHomeActivity
import com.example.online_pharmacy_app.databinding.FragmentAccountsBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CustomerViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Customer
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AccountsFragment : Fragment(R.layout.fragment_accounts), KodeinAware {

    private lateinit var binding: FragmentAccountsBinding
    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var skeleton: Skeleton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountsBinding.bind(view)
        initializeSkeleton()
        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        customerViewModel.customerDetails.observe(
            viewLifecycleOwner,
            Observer(::handleCustomerDetails)
        )


        binding.signOutTextView.setOnClickListener {
            customerViewModel.signOut().also {
                requireContext().startHomeActivity(FRAG_TO_OPEN)
            }
        }

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

                Glide.with(requireContext()).load(result.data.token)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImageView)

            }
            is SResult.Error -> {
                startActivity(Intent(context, GoogleSignInActivity::class.java))
            }
            is SResult.Empty -> {
                binding.customerNameTextView.text = "You don't have an account Yet!!!"
                binding.phoneTextView.visibility = View.GONE
                binding.emailTextView.visibility = View.GONE
                binding.editProfileTextView.visibility = View.INVISIBLE
                binding.signOutTextView.visibility = View.INVISIBLE
                startActivity(Intent(context, GoogleSignInActivity::class.java))
            }
        }
    }

    private fun initializeSkeleton() {
        binding.recyclerviewOtherDrugLists.layoutManager = LinearLayoutManager(context)
        skeleton = binding.skeletonAccounts
        skeleton =
            binding.recyclerviewOtherDrugLists.applySkeleton(R.layout.item_drug_list_small_view, 8)
        skeleton.showSkeleton()

    }
}