package com.pharmacure.online_pharmacy_app.activities.categories.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnAddToCartCategoryDrug
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnCategoryItemClickListener
import com.pharmacure.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.pharmacure.online_pharmacy_app.activities.viewholders.DrugByCategoryResultViewHolder
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.DrugViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import com.pharmacure.online_pharmacy_app.viewobjects.Drug
import kotlinx.android.synthetic.main.fragment_category_switcher.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(R.layout.fragment_category_switcher),
    OnDrugByCategoryResult,
   DIAware,
    OnCartChangeResultListeners {

    private lateinit var cartViewModal: CartViewModal
    private lateinit var pageViewModel: PageViewModel
    private lateinit var drugViewModel: DrugViewModel
    override val di: DI by di()
    private val factory: ViewModelFactory by instance()

    private lateinit var skeleton: Skeleton
    var QUANTITY = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }


        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        drugViewModel.onDrugsByCategoryResult = this
        drugViewModel.getDrugsByCategoryList(
            arguments?.getInt(CATEGORY)!!,
            pageViewModel.getIndex()!!
        )


        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)
        cartViewModal.onCartChangeResultList = this
    }


    private fun initializeSkeleton() {
        drugByCategoryResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        skeleton = skeletonSearchResults
        skeleton =
            drugByCategoryResultsRecyclerView.applySkeleton(R.layout.item_drug_list_small_view, 8)
        skeleton.showSkeleton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSkeleton()
        pageViewModel.text.observe(viewLifecycleOwner, Observer<String> {
            initializeSkeleton()
        })

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number and the current category ID that has been selected
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, categoryID: Int, customerID: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putInt(CATEGORY, categoryID)
                    putInt(CUSTOMER_ID, customerID)
                }
            }
        }
    }

    override fun handleDrugsByCategoryResult(result: SResult<List<Drug>>) {
        when (result) {
            is SResult.Loading -> skeleton.showSkeleton()
            is SResult.Success -> {

                result.data.isEmpty().let {
                    if (it) showStickerCategoryNotFound()
                    else hideStickerCategoryNotFound()
                }

                skeleton.showOriginal()
                SmartRecyclerAdapter
                    .items(result.data)
                    .map(Drug::class, DrugByCategoryResultViewHolder::class)
                    .addViewEventListener(object : OnAddToCartCategoryDrug {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position
                        ) {

                            arguments?.getInt(CUSTOMER_ID, 0).let { customer ->
                                if (customer != null) {
                                    cartViewModal.setAddToCart(
                                        QUANTITY,
                                        customer,
                                        result.data[position].drugID,
                                        result.data[position].unitPrice
                                    ).also {
                                        cartViewModal.addToCart()
                                    }
                                } else {
                                    context?.startLoginActivity()
                                }

                            }
                        }

                    })
                    .addViewEventListener(object : OnCategoryItemClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position
                        ) {
                            arguments?.getInt(CUSTOMER_ID).let { customerID ->
                                context?.startDrugDetailsPage(
                                    result.data[position].drugID,
                                    customerID!!
                                )
                            }
                        }
                    })
                    .into<SmartRecyclerAdapter>(drugByCategoryResultsRecyclerView)
            }
            is SResult.Error -> {
                showSnackBack("Error occurred ${result.message}")
                skeleton.showOriginal()
            }
            is SResult.Empty -> {
                showSnackBack("No response from server")
                skeleton.showOriginal()
            }
        }
    }

    private fun hideStickerCategoryNotFound() {
        constraintLayoutCategoryNotFound.visibility = View.INVISIBLE
    }

    private fun showStickerCategoryNotFound() {
        constraintLayoutCategoryNotFound.visibility = View.VISIBLE
    }

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> Toast.makeText(
                requireContext(),
                "Cart updated :${result.data.map { it.quantity }.sum()} items",
                Toast.LENGTH_SHORT
            ).show()
            is SResult.Error -> showSnackBack("error occurred ${result.message}")
            is SResult.Empty -> showSnackBack("No response from sever ")
        }
    }


    private fun showSnackBack(message: String) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show()
    }


}