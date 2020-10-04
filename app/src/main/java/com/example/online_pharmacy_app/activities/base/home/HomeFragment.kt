package com.example.online_pharmacy_app.activities.base.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.ItemListListners.OnAddToCartGridListItemClickListener
import com.example.online_pharmacy_app.activities.ItemListListners.OnDrugImageClickListener
import com.example.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.example.online_pharmacy_app.activities.OnUpdateCartListener
import com.example.online_pharmacy_app.activities.categories.CategorySwitcherActivity
import com.example.online_pharmacy_app.activities.viewholders.DrugsViewHolder
import com.example.online_pharmacy_app.common.*
import com.example.online_pharmacy_app.databinding.FragmentHomeBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Cart
import com.example.online_pharmacy_app.viewobjects.Drug
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_category_switcher.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId

class HomeFragment : Fragment(R.layout.fragment_home), KodeinAware, OnCartChangeResultListeners,
    View.OnClickListener {

    override val kodein: Kodein by closestKodein()
    private lateinit var binding: FragmentHomeBinding
    private val factory: ViewModelFactory by instance()
    private lateinit var drugViewModel: DrugViewModel
    private lateinit var cartViewModal: CartViewModal
    private lateinit var skeleton: Skeleton
    var updateCartListener: OnUpdateCartListener? = null
    var customerId: Int? = null
    private val QUANTITY: Int = 1
    var progressBarCart: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)


        binding.supplementsButton.setOnClickListener(this)
        binding.herbalImageButton.setOnClickListener(this)
        binding.cosmeticsImageButton.setOnClickListener(this)
        binding.otpImageButton.setOnClickListener(this)
        binding.prescreptionOnlyImageButton.setOnClickListener(this)
        binding.nationalDrugRegisterImageButton.setOnClickListener(this)

        drugViewModel.getDrugList().observe(viewLifecycleOwner, Observer(::handleDrugResults))
        cartViewModal.onCartChangeResultList = this
        initializeSkeleton()
    }


    private fun initializeSkeleton() {
        binding.recyclerViewDrugsHome.layoutManager = GridLayoutManager(context, 2)
        skeleton = binding.skeletonHome
        skeleton = binding.recyclerViewDrugsHome.applySkeleton(R.layout.item_drug_gridview, 6)
        skeleton.showSkeleton()
    }

    private fun handleDrugResults(result: SResult<List<Drug>>) {
        when (result) {
            is SResult.Loading -> {
                log { "Loading..." }
            }

            is SResult.Success -> {
                log { "Result ${result.data}" }
                skeleton.showOriginal()
                SmartRecyclerAdapter
                    .items(result.data)
                    .map(Drug::class, DrugsViewHolder::class)
                    .setLayoutManager(GridLayoutManager(requireContext(), 2))
                    .addViewEventListener(object : OnDrugImageClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position
                        ) {
                            customerId.let {
                                if(it !=null){
                                    context?.startDrugDetailsPage(result.data[position].drugID, customerId!!
                                    )
                                }else { this@HomeFragment.context?.startLoginActivity()
                                }
                            }
                        }

                    })
                    .addViewEventListener(object : OnAddToCartGridListItemClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position
                        ) {
                            customerId.let { customer ->
                                if (customer != null) {
                                    cartViewModal.setAddToCart(
                                        QUANTITY,
                                        customerId!!,
                                        result.data[position].drugID,
                                        result.data[position].unitPrice
                                    ).also {
                                        cartViewModal.addToCart()
                                    }
                                } else {
                                    this@HomeFragment.context?.startLoginActivity()
                                }

                            }

                        }

                    })
                    .into<SmartRecyclerAdapter>(binding.recyclerViewDrugsHome)

            }
            is SResult.Error -> {
                log { "Error ${result.message} Message ${result.code}" }
            }

            is SResult.Empty -> {
               showSnackBack("No response from sever")
            }
        }
    }


    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> showProgressBar()
            is SResult.Success -> updateCartListener?.updateHotCount(result.data.map { it.quantity }
                .sum()).also {
                hideProgressBar()
            }
            is SResult.Error -> {
                hideProgressBar()
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
            }
            is SResult.Empty -> {
                hideProgressBar()
            showSnackBack("No response from server")
            }
        }
    }

    private fun showProgressBar() {
        progressBarCart?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarCart?.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.supplementsButton -> {
                toCategories(1)
            }

            binding.herbalImageButton -> {
                toCategories(2)
            }

            binding.cosmeticsImageButton -> {
                toCategories(5)
            }

            binding.otpImageButton -> {
                toCategories(4)
            }

            binding.prescreptionOnlyImageButton -> {
                toCategories(3)
            }

            binding.nationalDrugRegisterImageButton -> {
                toCategories(6)
            }

        }
    }

    private fun toCategories(categoryID: Int) {
        Intent(requireContext(), CategorySwitcherActivity::class.java).apply {
            putExtra(CATEGORY, categoryID)
            putExtra(CUSTOMER_ID,customerId)
            startActivity(Intent(this))
        }

    }

    fun showSnackBack(message:String){
        Snackbar.make(homeCoordinateLayout, message, Snackbar.LENGTH_SHORT).apply { show() }
    }


}