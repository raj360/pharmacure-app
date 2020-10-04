package com.example.online_pharmacy_app.activities.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.online_pharmacy_app.BuildConfig
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.ItemListListners.OnDecreaseQuantityClickListener
import com.example.online_pharmacy_app.activities.ItemListListners.OnIncreaseQuantityClickListener
import com.example.online_pharmacy_app.activities.ItemListListners.OnRemoveFromCartClickListener
import com.example.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.example.online_pharmacy_app.activities.OnUpdateCartListener
import com.example.online_pharmacy_app.activities.base.home.HomeFragment
import com.example.online_pharmacy_app.activities.viewholders.CartViewHolder
import com.example.online_pharmacy_app.common.FRAG_TO_OPEN
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.common.startHomeActivity
import com.example.online_pharmacy_app.common.startLoginActivity
import com.example.online_pharmacy_app.databinding.FragmentCartViewBinding
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Cart
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import com.nsiimbi.easypay.Request
import com.nsiimbi.easypay.Request.EP_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_drug_details.*
import kotlinx.android.synthetic.main.fragment_cart_view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId
import java.util.*
import kotlin.math.log10


class CartViewFragment : Fragment(R.layout.fragment_cart_view), KodeinAware,
    OnCartChangeResultListeners {

    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    private lateinit var cartViewModal: CartViewModal
    var customerId: Int? = null
    var updateCartListener: OnUpdateCartListener? = null
    private lateinit var binding: FragmentCartViewBinding
    private lateinit var homeFragment: HomeFragment
    private var MM = "Mobile Money"
    private var OD = "On delivery"
    private var OS = "On site"
    private val DELIVERY_COST = 5000
    private var TOTAL_CART_COST = 0
    private var selectedMethod = MM
    private var storeID: Int = 2
    private var cartTracker: MutableLiveData<List<Cart>> = MutableLiveData()
    lateinit var skeleton: Skeleton
    private var SECRETE = BuildConfig.SECRET
    private var CLIENT_ID = BuildConfig.CLIENT_ID
    var progressBarCart: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartViewBinding.bind(view)
        homeFragment = HomeFragment()
        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)
        cartViewModal.onCartChangeResultList = this
        initializeSkeleton()
        customerId.also {
            if (it != null) {
                cartViewModal.setCustomerId(it)
                cartViewModal.getCartData()
            } else {
                requireContext().startLoginActivity()
            }
        }



        binding.paymentMethodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            handleSelectOption(checkedId)
        }


        binding.orderNowButton.setOnClickListener {


            handleOrders()
        }

        /**
         * Button to exit to the home page
         */
        binding.exitCartButton.setOnClickListener {
            requireContext().startHomeActivity(FRAG_TO_OPEN)
        }


    }


    private fun initializeSkeleton() {
        binding.allCartItemsRecyclerView.layoutManager = LinearLayoutManager(context)
        skeleton = binding.skeletonCartList
        skeleton = binding.allCartItemsRecyclerView.applySkeleton(R.layout.item_drug_cart, 1)
        skeleton.showSkeleton()
    }


    private fun uniqueId() = UUID.randomUUID().toString()


    private fun handleOrders() {
        when {
            binding.editTextLocation.text.isEmpty() -> binding.editTextLocation.error =
                "Please Enter pickup address"
            binding.editTextLocation.text.length <= 3 -> binding.editTextLocation.error =
                "Invalid location value"
            else -> {
                /**
                 * customer id
                 * amount
                 * location
                 */

                Request(requireActivity())
                    .setAmountToPay("${TOTAL_CART_COST + DELIVERY_COST}")
                    .setCurrency("UGX")
                    .setPaymentReason("Pharmacure online pharmacy")
                    .setClientSecret(SECRETE)
                    .setClientID(CLIENT_ID)
                    .initialize();

            }
        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EP_REQUEST_CODE && resultCode == RESULT_OK) {
            val stringResponse = data!!.getStringExtra("response")
            Toast.makeText(requireContext(), stringResponse, Toast.LENGTH_SHORT).show()
            "On Success ${data.getStringExtra("response")}"

            cartTracker.value.let {cartList ->
                cartList?.map{ it.cartID }?.toIntArray()?.let {cartIDs ->
                    cartViewModal.setMakeOrder(
                        customerId!!,
                        cartIDs,
                        selectedMethod,
                        binding.editTextLocation.text.toString()
                    )
                }
//                cartViewModal.makeOrder()
            }

        }else{
            log { "On Error ${data?.getStringExtra("response")}" }
            showSnackBack("Order was not placed try again")
        }
    }



    private fun handleSelectOption(checkedId: Int) {
        when (checkedId) {
            R.id.mobileMoneyRadioButton -> {
                selectedMethod = MM
                binding.deliveryCost.text = DELIVERY_COST.toString()
                binding.orderTotalAmount.text = (DELIVERY_COST + TOTAL_CART_COST).toString()
            }
            R.id.onDeliveryRadioButton -> {
                selectedMethod = OD
                binding.deliveryCost.text = DELIVERY_COST.toString()
                binding.orderTotalAmount.text = (DELIVERY_COST + TOTAL_CART_COST).toString()
            }
            R.id.defaultRadioButton -> {
                selectedMethod = OS
                binding.deliveryCost.text = 0.toString()
                binding.orderTotalAmount.text = (TOTAL_CART_COST).toString()
            }
        }
    }

    private fun loadCartList(result: SResult.Success<List<Cart>>) {
        /**
         * Smart adapter to do with loading our items to the recycler views without
         * using having to write any adapter code
         */
        skeleton.showOriginal()
        getCartTotalSum(result.data)
        SmartRecyclerAdapter
            .items(result.data)
            .map(Cart::class, CartViewHolder::class)
            .setLayoutManager(LinearLayoutManager(requireContext()))
            .addViewEventListener(object : OnRemoveFromCartClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    customerId.let {
                        cartViewModal.setDeleteCart(it!!, result.data[position].cartID)
                            .also { cartViewModal.deleteCart() }
                    }
                }
            }).addViewEventListener(object : OnIncreaseQuantityClickListener {
                override fun onViewEvent(
                    view: View, viewEventId: ViewEventId, position: Position
                ) {
                    customerId.let {
                        cartViewModal.setUpdateCart(
                            it!!,
                            result.data[position].quantity + 1,
                            result.data[position].cartID
                        ).also {
                            cartViewModal.updateCart()
                        }
                    }
                }

            }).addViewEventListener(object : OnDecreaseQuantityClickListener {
                override fun onViewEvent(view: View, viewEventId: ViewEventId, position: Position) {
                    customerId.let {
                        if (result.data[position].quantity > 1) {
                            cartViewModal.setUpdateCart(
                                it!!,
                                result.data[position].quantity - 1,
                                result.data[position].cartID
                            ).also {
                                cartViewModal.updateCart()
                            }

                        }
                    }
                }

            })
            .into<SmartRecyclerAdapter>(binding.allCartItemsRecyclerView).let {
                binding.allCartItemsRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> {
                showProgressBar()
                skeleton.showSkeleton()
            }
            is SResult.Success -> {
                loadCartList(result)
                updateCartListener?.updateHotCount(result.data.map { it.quantity }.sum()).also {
                    hideProgressBar()
                }
            }
            is SResult.Error -> {
                hideProgressBar()
                skeleton.showSkeleton()
            }
            is SResult.Empty -> {
                hideProgressBar()
                showSnackBack("No response from sever")
                skeleton.showSkeleton()
            }
        }

    }


    private fun getCartTotalSum(cartList: List<Cart>) {
        cartList.let { list ->
            cartTracker.value = list
            if (list.isNotEmpty())
                list.map { cart -> cart.costPrice }
                    .also {
                        binding.cartTotalAmount.text = it.sum().toString()
                        TOTAL_CART_COST = it.sum()
                        when (selectedMethod) {
                            MM -> {
                                binding.deliveryCost.text = DELIVERY_COST.toString()
                                binding.orderTotalAmount.text =
                                    (DELIVERY_COST + TOTAL_CART_COST).toString()
                            }
                            OD -> {
                                binding.deliveryCost.text = DELIVERY_COST.toString()
                                binding.orderTotalAmount.text =
                                    (DELIVERY_COST + TOTAL_CART_COST).toString()
                            }
                        }
                    }
            else {
                binding.cartTotalAmount.text = 0.toString()
                binding.orderTotalAmount.text = 0.toString()
                binding.deliveryCost.text = 0.toString()
            }
        }

    }

    private fun showProgressBar() {
        progressBarCart?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarCart?.visibility = View.GONE
    }

    fun showSnackBack(message:String){
        Snackbar.make(nestedScrollViewCartView, message, Snackbar.LENGTH_SHORT).apply { show() }
    }


}


