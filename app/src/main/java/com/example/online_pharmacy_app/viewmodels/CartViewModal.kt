package com.example.online_pharmacy_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.example.online_pharmacy_app.domain.repository.CartRepository
import kotlinx.coroutines.launch


class CartViewModal(
    private val repository: CartRepository
) : ViewModel() {

    private var quantity: MutableLiveData<Int> = MutableLiveData()
    private var customerId: MutableLiveData<Int> = MutableLiveData()
    private var drugID: MutableLiveData<Int> = MutableLiveData()
    private var cartId: MutableLiveData<Int> = MutableLiveData()
    private var cartIds: MutableLiveData<IntArray> = MutableLiveData()
    private var deliveryMode: MutableLiveData<String> = MutableLiveData()
    private var location: MutableLiveData<String> = MutableLiveData()
    private var costPrice: MutableLiveData<Int> = MutableLiveData()


    var onCartChangeResultList: OnCartChangeResultListeners? = null


    /**
     * code to add to cart
     */
    fun setAddToCart(
        quantity: Int,
        customerID: Int,
        drugID: Int,
        costPrice: Int
    ) {

        this.quantity.value = quantity
        this.customerId.value = customerID
        this.drugID.value = drugID
        this.costPrice.value = costPrice
    }

    fun addToCart() = viewModelScope.launch {
        repository.addToCart(
            quantity.value!!,
            customerId.value!!,
            drugID.value!!,
            costPrice.value!!
        ).also {
            onCartChangeResultList?.handleCartResult(it)
        }
    }

    /**
     * code to update the shopping cart
     */
    fun setUpdateCart(
        customerID: Int,
        quantity: Int,
        cartID: Int
    ) {
        this.customerId.value = customerID
        this.quantity.value = quantity
        this.cartId.value = cartID
    }

    fun updateCart() {
        viewModelScope.launch {
            repository.updateCart(customerId.value!!, quantity.value!!, cartId.value!!).also {
                onCartChangeResultList?.handleCartResult(it)
            }

        }
    }

    fun setDeleteCart(customerID: Int, cartID: Int) {
        this.customerId.value = customerID
        this.cartId.value = cartID
    }

    fun deleteCart() =
        viewModelScope.launch {
            repository.deleteFromCart(customerId.value!!, cartId.value!!).also {
                onCartChangeResultList?.handleCartResult(it)
            }
        }


    fun setMakeOrder(
        customerId: Int,
        cartIds: IntArray,
        deliveryMode: String,
        location: String
    ) {
        this.customerId.value = customerId
        this.cartIds.value = cartIds
        this.deliveryMode.value = deliveryMode
        this.location.value = location

    }
//
//    fun makeOrder() = viewModelScope.launch {
//            repository.makeOrders(
//                customerId.value!!,
//                cartIds.value!!,
//                deliveryMode.value!!,
//                location.value!!
//            )
//        }


    fun setCustomerId(customerId: Int) {
        this.customerId.value = customerId
    }

    fun getCartData() = viewModelScope.launch {
        repository.getRemoteCartList(customerId.value!!).also {
            onCartChangeResultList?.handleCartResult(it)
        }
    }

    fun clearCart(customerID: Int) = viewModelScope.launch {
        repository.clearCart(customerId.value!!).also {
            onCartChangeResultList?.handleCartResult(it)
        }
    }

}