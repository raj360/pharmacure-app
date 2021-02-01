package com.pharmacure.online_pharmacy_app.activities.base.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.viewholders.PaymentDetailsViewHolder
import com.pharmacure.online_pharmacy_app.common.log
import com.pharmacure.online_pharmacy_app.common.startLoginActivity
import com.pharmacure.online_pharmacy_app.databinding.FragmentOrdersBinding
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewobjects.Order
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.pharmacure.online_pharmacy_app.activities.OnShowSnackBarListeners
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import smartadapter.SmartRecyclerAdapter


class OrdersFragment : Fragment(R.layout.fragment_orders), DIAware {

    override val di: DI by di()
    private val factory: ViewModelFactory by instance()
    var customerId: Int? = null
    lateinit var binding: FragmentOrdersBinding
    private lateinit var skeleton: Skeleton
    var customerViewModel: CustomerViewModel? = null
     lateinit var onShowSnackBarListeners:OnShowSnackBarListeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrdersBinding.bind(view)
        initializeSkeleton()

        if ((customerId != null)) {
            customerViewModel?.also {
                it.getOrderDetails(customerId!!)
                    .observe(viewLifecycleOwner, Observer(::handleOrderDetailsResult))
            }
        } else {
            requireContext().startLoginActivity()
        }


    }

    private fun handleOrderDetailsResult(result: SResult<List<Order>>) {
        when (result) {
            is SResult.Loading -> {
                hideStickerNoResults()
                log { "Loading..." }
            }

            is SResult.Success -> {


                if(result.data.isEmpty()){
                    showStickerNoResults()
                    skeleton.showOriginal()
                }else{
                    hideStickerNoResults()
                    skeleton.showOriginal()
                    SmartRecyclerAdapter
                        .items(result.data)
                        .map(Order::class, PaymentDetailsViewHolder::class)
                        .into<SmartRecyclerAdapter>(binding.recyclerOrdersPage)
                }
            }

            is SResult.Empty -> {
                skeleton.showOriginal()
                showStickerNoResults()
               onShowSnackBarListeners.showSnackBack("No response from server")
            }

            is SResult.Error -> {
                showStickerNoResults()
                Toast.makeText(
                    requireContext(),
                    "Error occurred: ${result.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initializeSkeleton() {
        binding.recyclerOrdersPage.layoutManager = LinearLayoutManager(context)
        skeleton = binding.skeletonOrdersPage
        skeleton = binding.recyclerOrdersPage.applySkeleton(R.layout.item_drug_list_small_view, 5)
        skeleton.showSkeleton()
    }

    private fun showStickerNoResults() {
        binding.cardViewStickerOutOfOrders.visibility = View.VISIBLE
    }

    private fun hideStickerNoResults() {
        binding.cardViewStickerOutOfOrders.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun getInstance() =  OrdersFragment()
    }

}