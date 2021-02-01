package com.pharmacure.online_pharmacy_app.activities.base.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnAddToCartGridListItemClickListener
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnDrugImageClickListener
import com.pharmacure.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.pharmacure.online_pharmacy_app.activities.OnShowSnackBarListeners
import com.pharmacure.online_pharmacy_app.activities.OnUpdateCartListener
import com.pharmacure.online_pharmacy_app.activities.OrderByPrescriptionActivity
import com.pharmacure.online_pharmacy_app.activities.categories.CategorySwitcherActivity
import com.pharmacure.online_pharmacy_app.activities.viewholders.DrugsViewHolder
import com.pharmacure.online_pharmacy_app.common.CATEGORY
import com.pharmacure.online_pharmacy_app.common.CUSTOMER_ID
import com.pharmacure.online_pharmacy_app.common.startDrugDetailsPage
import com.pharmacure.online_pharmacy_app.common.startLoginActivity
import com.pharmacure.online_pharmacy_app.databinding.FragmentHomeBinding
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.DrugViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId


class HomeFragment : Fragment(R.layout.fragment_home),OnCartChangeResultListeners,
    View.OnClickListener,DIAware {

    override val di: DI by di()
    private val factory: ViewModelFactory by instance()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var drugViewModel: DrugViewModel
    private lateinit var cartViewModal: CartViewModal
    private lateinit var skeleton: Skeleton
    var updateCartListener: OnUpdateCartListener? = null
    lateinit var onShowSnackBarListeners: OnShowSnackBarListeners
    var customerId: Int? = null
    private val QUANTITY: Int = 1




    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        binding.orderByPrescriptionButton.setOnClickListener {
            val intent = Intent(requireContext(), OrderByPrescriptionActivity::class.java)
            if (customerId != null) {
                startActivity(intent.putExtra(CUSTOMER_ID, customerId!!))
            } else {
                requireContext().startLoginActivity()
            }

        }

        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

            binding.itemsswipetorefresh.setOnRefreshListener {
                Toast.makeText(requireContext(),"Updated ",Toast.LENGTH_SHORT).show()
                drugViewModel.getDrugList().observe(viewLifecycleOwner, Observer(::handleDrugResults))
                Handler().postDelayed({
                    binding.itemsswipetorefresh.isRefreshing = false
                },2000)
            }





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
                skeleton.showSkeleton()
            }

            is SResult.Success -> {
                skeleton.showOriginal()
                SmartRecyclerAdapter
                    .items(result.data)
                    .map(Drug::class, DrugsViewHolder::class)
                    .setLayoutManager(GridLayoutManager(requireContext(), 2))
                    .addViewEventListener(object : OnDrugImageClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position,
                        ) {
                            context?.startDrugDetailsPage(result.data[position].drugID,
                                customerId ?: 0)

                        }

                    })
                    .addViewEventListener(object : OnAddToCartGridListItemClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position,
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
                onShowSnackBarListeners.showSnackBack("Error : ${result.message}")
            }

            is SResult.Empty -> {
                onShowSnackBarListeners.showSnackBack("No response from sever")
            }
        }
    }


    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> {
                updateCartListener?.showCartProgress()
            }
            is SResult.Success -> {
                updateCartListener?.hideCartProgress()
                updateCartListener?.updateHotCount(result.data.map { it.quantity }.sum())

            }
            is SResult.Error -> {
                updateCartListener?.hideCartProgress()
                updateCartListener?.updateHotCount(0)
                onShowSnackBarListeners.showSnackBack("Error occurred: ${result.message}")

            }
            is SResult.Empty -> {
                updateCartListener?.hideCartProgress()
                updateCartListener?.updateHotCount(0)
                onShowSnackBarListeners.showSnackBack("No response from sever")
            }
        }
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
            putExtra(CUSTOMER_ID, customerId)
            startActivity(Intent(this))
        }

    }
//
//    companion object {
//        @JvmStatic
//        fun getInstance(): HomeFragment {
//            return HomeFragment()
//        }
//    }








}