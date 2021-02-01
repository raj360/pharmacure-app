package com.pharmacure.online_pharmacy_app.activities.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.pharmacure.online_pharmacy_app.BuildConfig
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnDecreaseQuantityClickListener
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnIncreaseQuantityClickListener
import com.pharmacure.online_pharmacy_app.activities.ItemListListners.OnRemoveFromCartClickListener
import com.pharmacure.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.pharmacure.online_pharmacy_app.activities.OnShowSnackBarListeners
import com.pharmacure.online_pharmacy_app.activities.OnUpdateCartListener
import com.pharmacure.online_pharmacy_app.activities.base.home.HomeFragment
import com.pharmacure.online_pharmacy_app.activities.viewholders.CartViewHolder
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.databinding.FragmentCartViewBinding
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.ApiResponse
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.rasalexman.coroutinesmanager.CoroutinesProvider.UI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.ViewEventId
import java.util.*
import kotlin.coroutines.CoroutineContext

class CartViewFragment : Fragment(R.layout.fragment_cart_view), DIAware,
    OnCartChangeResultListeners, CoroutineScope {

    private var progressDialog: ProgressDialog? = null
    var customerViewModel: CustomerViewModel? = null
    override val di: DI by di()
    private val factory: ViewModelFactory by instance()
    private lateinit var cartViewModal: CartViewModal
    var customerId: Int? = null
    var customer: Customer? = null
    var updateCartListener: OnUpdateCartListener? = null

    private lateinit var binding: FragmentCartViewBinding
    private lateinit var homeFragment: HomeFragment
    private var MM = "MOBILE_MONEY"
    private var OD = "ON_DELIVERY_PAYMENT"
    private var OS = "CALLED_TO_ORDER"
    private val DELIVERY_COST = 5000
    private var TOTAL_CART_COST = 0
    private var selectedMethod: MutableLiveData<String> = MutableLiveData()
    private var cartTracker: MutableLiveData<List<Cart>> = MutableLiveData()
    var userUUID: MutableLiveData<String> = MutableLiveData()
    lateinit var skeleton: Skeleton
    private var PUBLIC_KEY = BuildConfig.PUBLIC_KEY
    private var SECRET_KEY = BuildConfig.SECRETE_KEY
    private var ENCRYPTION_KEY = BuildConfig.ENCRYPTION_KEY

    private val TELEPHONE_SCHEMA = "tel:"
    private val PRESERVED_CHARACTER = "+"
    private val HK_COUNTRY_CODE = "256"
    private val HK_OBSERVATORY_PHONE_NUMBER = "7000000000"

    lateinit var onShowSnackBarListeners: OnShowSnackBarListeners

    var PENDING = "PENDING"
    var PAID = "PAID"
    var UNPAID = "UNPAID"

    /**
    payment statuses
    0 =>
    1 =>
    2 =>
     */

    var amount: Double = 0.0
    var paymentStatus: String = ""
    var paymentRef: String = ""
    var customerID: Int = 0
    var txRef: String = ""
    var orderRef: String = ""
    var charge: Double = 0.0
    var deliveryMethod: String = ""
    lateinit var itemIDs: IntArray
    var location: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartViewBinding.bind(view)
        homeFragment = HomeFragment()
        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)
        cartViewModal.onCartChangeResultList = this
        initializeSkeleton()


        selectedMethod.value = MM

        userUUID.value = uniqueId()

        progressDialog = ProgressDialog(requireContext())


        customerId.also {
            if ((it != null) && customer != null) {
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
            if (isLocationFieldEmpty()) {
                binding.editTextLocation.error = "Please Enter pickup address"
            } else {
                handleOrders()
            }

        }

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


    private fun handleOrders() {
        when (selectedMethod.value) {
            OS -> {
                amount = (TOTAL_CART_COST + DELIVERY_COST).toDouble()
                paymentStatus = UNPAID
                paymentRef = ""
                customerID = customer?.customerID!!
                txRef = uniqueId()
                orderRef = ""
                charge = 0.0
                deliveryMethod = OS
                itemIDs = cartTracker.value?.map { it.cartID }?.toIntArray()!!
                location = binding.editTextLocation.text.toString()

                customerViewModel?.setMakeOrder(
                    amount,
                    paymentStatus,
                    paymentRef,
                    customerID,
                    txRef,
                    orderRef,
                    charge,
                    deliveryMethod,
                    itemIDs,
                    location
                )

                launch(UI) {
                    customerViewModel?.makeOrder().also {
                        handleOrdersResult(it)
                    }

                }

            }
            OD -> {
                amount = (TOTAL_CART_COST + DELIVERY_COST).toDouble()
                paymentStatus = PENDING
                paymentRef = ""
                customerID = customer?.customerID!!
                txRef = userUUID.value!!
                orderRef = ""
                charge = 0.0
                deliveryMethod = OD
                itemIDs = cartTracker.value?.map { it.cartID }?.toIntArray()!!
                location = binding.editTextLocation.text.toString()

                customerViewModel?.setMakeOrder(
                    amount,
                    paymentStatus,
                    paymentRef,
                    customerID,
                    txRef,
                    orderRef,
                    charge,
                    deliveryMethod,
                    itemIDs,
                    location
                )

                launch(UI) {
                    customerViewModel?.makeOrder().also {
                        handleOrdersResult(it)
                    }
                }

            }
            else -> {
                amount = (TOTAL_CART_COST + DELIVERY_COST).toDouble()
                paymentStatus = PAID
                paymentRef = customer?.telephone + Date().toString()
                customerID = customer?.customerID!!
                txRef = userUUID.value!!
                orderRef = customer?.email ?: ""
                charge = 0.0
                deliveryMethod = MM
                itemIDs = cartTracker.value?.map { it.cartID }?.toIntArray()!!
                location = binding.editTextLocation.text.toString()


                deliveryMethod = MM
                itemIDs = cartTracker.value?.map { it.cartID }?.toIntArray()!!
                location = binding.editTextLocation.text.toString()
                customerViewModel?.setMakeOrder(
                    amount,
                    paymentStatus,
                    paymentRef,
                    customerID,
                    txRef,
                    orderRef,
                    charge,
                    deliveryMethod,
                    itemIDs,
                    location
                )


                RaveUiManager(this)
                    .setAmount((TOTAL_CART_COST + DELIVERY_COST).toDouble())
                    .setCurrency("UGX")
                    .setEmail(customer?.email)
                    .setfName(customer?.fullName)
                    .setlName("")
                    .setPublicKey(PUBLIC_KEY)
                    .setEncryptionKey(ENCRYPTION_KEY)
                    .setTxRef(uniqueId())
                    .acceptAccountPayments(false)
                    .acceptCardPayments(false)
                    .acceptMpesaPayments(false)
                    .acceptAchPayments(false)
                    .acceptGHMobileMoneyPayments(false)
                    .acceptUgMobileMoneyPayments(true)
                    .acceptZmMobileMoneyPayments(false)
                    .acceptRwfMobileMoneyPayments(false)
                    .acceptSaBankPayments(false)
                    .acceptUkPayments(false)
                    .acceptBankTransferPayments(false)
                    .acceptUssdPayments(false)
                    .acceptBarterPayments(false)
                    .acceptFrancMobileMoneyPayments(false)
                    .allowSaveCardFeature(false)
                    .onStagingEnv(false)
                    .withTheme(R.style.RaveViewTheme)
                    .isPreAuth(false)
                    .shouldDisplayFee(true)
                    .showStagingLabel(true)
                    .initialize()
            }


        }


    }


    private fun orderOnPhone() {
        val phoneCallUri =
            Uri.parse(TELEPHONE_SCHEMA + PRESERVED_CHARACTER + HK_COUNTRY_CODE + HK_OBSERVATORY_PHONE_NUMBER)
        val phoneCallIntent = Intent(Intent.ACTION_DIAL).also {
            it.data = phoneCallUri
        }
        startActivity(phoneCallIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            log { "Message from server $message" }
            when (resultCode) {
                RavePayActivity.RESULT_SUCCESS -> {

                    Toast.makeText(requireContext(), "PAYMENT SUCCESS", Toast.LENGTH_LONG).show()

                    launch(UI) {
                        customerViewModel?.makeOrder().also {
                            handleOrdersResult(it)
                        }
                    }

                }
                RavePayActivity.RESULT_ERROR -> {
                    onShowSnackBarListeners.showSnackBack("ERROR $message")
                }
                RavePayActivity.RESULT_CANCELLED -> {
                    onShowSnackBarListeners.showSnackBack("CANCELLED $message")
                }
            }
        } else {
            onShowSnackBarListeners.showSnackBack("Request code not good")
        }
    }


    private fun handleSelectOption(checkedId: Int) {
        when (checkedId) {
            R.id.mobileMoneyRadioButton -> {
                selectedMethod.value = MM
                binding.deliveryCost.text = DELIVERY_COST.toString()
                binding.orderTotalAmount.text = (DELIVERY_COST + TOTAL_CART_COST).toString()
                binding.orderNowButton.text = getString(R.string.order_now)
            }
            R.id.onDeliveryRadioButton -> {
                selectedMethod.value = OD
                binding.deliveryCost.text = DELIVERY_COST.toString()
                binding.orderTotalAmount.text = (DELIVERY_COST + TOTAL_CART_COST).toString()
                binding.orderNowButton.text = getString(R.string.order_now)
            }
            R.id.defaultRadioButton -> {
                selectedMethod.value = OS
                binding.deliveryCost.text = 0.toString()
                binding.orderTotalAmount.text = (TOTAL_CART_COST).toString()
                binding.orderNowButton.text = getString(R.string.call_to_order)


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

            }).into<SmartRecyclerAdapter>(binding.allCartItemsRecyclerView).let {
                binding.allCartItemsRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    private fun handleOrdersResult(result: SResult<ApiResponse>?) {
        when (result) {
            is SResult.Loading -> {
                showProgressDialog(progressDialog, "Placing order....")
            }

            is SResult.Success -> {
                hideProgressDialog(progressDialog)
                when {
                    result.data.error -> {
                        onShowSnackBarListeners.showSnackBack("Error occurred:${result.data.message}")
                    }
                    selectedMethod.value == OS -> {
                        orderOnPhone()
                    }
                    else -> {
                        requireContext().startHomeActivity(OPEN_ORDERS_FRAG)
                    }
                }
            }

            is SResult.Error -> {
                hideProgressDialog(progressDialog)
                showProgressDialog(progressDialog, "Error occurred:${result.message}")
            }

            is SResult.Empty -> {
                hideProgressDialog(progressDialog)
                onShowSnackBarListeners.showSnackBack("Order was not placed")
            }
        }
    }

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> {
                updateCartListener?.showCartProgress()
                skeleton.showSkeleton()
            }
            is SResult.Success -> {
                if (result.data.isNotEmpty()) {
                    loadCartList(result)
                    updateCartListener?.hideCartProgress()
                    updateCartListener?.updateHotCount(result.data.map { it.quantity }.sum())
                } else {
                    Toast.makeText(requireContext(),"Your cart is empty",Toast.LENGTH_SHORT).show()
                    requireContext().startHomeActivity(FRAG_TO_OPEN)
                }


            }
            is SResult.Error -> {
                updateCartListener?.updateHotCount(0)
                updateCartListener?.hideCartProgress()
                skeleton.showSkeleton()
            }
            is SResult.Empty -> {
                updateCartListener?.updateHotCount(0)
                updateCartListener?.hideCartProgress()
                onShowSnackBarListeners.showSnackBack("No response from sever")
                skeleton.showSkeleton()
            }
        }

    }


    private fun getCartTotalSum(cartList: List<Cart>) {
        cartList.let { list ->
            cartTracker.value = list
            cartTracker.value!!.isEmpty().also {
                binding.orderNowButton.isEnabled = !it
//Tracking if cart is empty
            }
            if (list.isNotEmpty())
                list.map { cart -> cart.costPrice }
                    .also {
                        binding.cartTotalAmount.text = it.sum().toString()
                        TOTAL_CART_COST = it.sum()
                        when (selectedMethod.value!!) {
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
//crashlytics
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


    private fun isLocationFieldEmpty() = binding.editTextLocation.text.isNullOrEmpty()


    companion object {
        @JvmStatic
        fun getInstance() = CartViewFragment()
    }

}


