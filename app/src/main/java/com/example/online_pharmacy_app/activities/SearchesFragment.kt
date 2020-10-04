package com.example.online_pharmacy_app.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.ItemListListners.OnAddToCartSearchListItemClickListener
import com.example.online_pharmacy_app.activities.viewholders.SearchResultDrugViewHolder
import com.example.online_pharmacy_app.common.startLoginActivity
import com.example.online_pharmacy_app.databinding.FragmentSearchesBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Drug
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.android.synthetic.main.fragment_searches.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId


class SearchesFragment : Fragment(R.layout.fragment_searches), OnSearchDrugListener{

    var drugViewModel: DrugViewModel? =null
    var cartViewModal: CartViewModal? =null
    private lateinit var binding: FragmentSearchesBinding
    private lateinit var skeleton: Skeleton
    var updateCartListener: OnUpdateCartListener? = null
    var customerId: Int? = null
    var QUANTITY = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drugViewModel?.onSearchDrugListener =this
        binding = FragmentSearchesBinding.bind(view)
        initializeSkeleton()
    }

    private fun initializeSkeleton() {
        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
        skeleton = binding.skeletonSearchResults
        skeleton = binding.searchResultsRecyclerView.applySkeleton(R.layout.item_drug_list_small_view, 5)
        skeleton.showSkeleton()
    }


    override fun searchDrugResult(result: SResult<List<Drug>>) {
        when (result) {
            is SResult.Loading -> initializeSkeleton()
            is SResult.Success -> {
                skeleton.showOriginal()
                result.data.isEmpty().let {
                    if(it) showStickerNoResults()
                    else hideStickerNoResults()
                }

                SmartRecyclerAdapter
                    .items(result.data)
                    .map(Drug::class, SearchResultDrugViewHolder::class)
                    .addViewEventListener(object : OnAddToCartSearchListItemClickListener {
                        override fun onViewEvent(
                            view: View,
                            viewEventId: ViewEventId,
                            position: Position
                        ) {
                            customerId.let { customer ->
                                if (customer != null) {
                                    cartViewModal?.setAddToCart(
                                        QUANTITY,
                                        customerId!!,
                                        result.data[position].drugID,
                                        result.data[position].unitPrice
                                    )
                                        .also {
                                            cartViewModal?.addToCart()
                                        }
                                } else {
                                    this@SearchesFragment.context?.startLoginActivity()
                                }

                            }
                        }

                    })
                    .into<SmartRecyclerAdapter>(binding.searchResultsRecyclerView)
            }
            is SResult.Error -> {
                skeleton.showOriginal()
                showStickerNoResults()
            }
            is SResult.Empty -> {
                skeleton.showOriginal()
                showStickerNoResults()}
        }
    }

    private fun showStickerNoResults() {
        constraintLayoutSearchNotFound.visibility = View.VISIBLE
    }

    private fun hideStickerNoResults() {
     constraintLayoutSearchNotFound.visibility = View.GONE
    }


}