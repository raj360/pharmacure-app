package com.pharmacure.online_pharmacy_app.viewmodels

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.*
import com.pharmacure.online_pharmacy_app.activities.OnCustomerSignInResultListeners
import com.pharmacure.online_pharmacy_app.common.startLoginActivity
import com.pharmacure.online_pharmacy_app.common.startSignUpActivity
import com.pharmacure.online_pharmacy_app.domain.repository.CustomerRepository
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.result.loading
import com.pharmacure.online_pharmacy_app.viewobjects.ApiResponse
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.pharmacure.online_pharmacy_app.viewobjects.Order
import com.pharmacure.online_pharmacy_app.viewobjects.OrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    var firstName: String = ""
    var lastName: String = ""
    var emailAddress: String = ""
    var password: String = ""
    var telephone: String = ""


    //This listener updates helps to update the PhoneVerification process the user data has been
    //saved to the remote and the local server
    var onCustomerSignInResultListeners: OnCustomerSignInResultListeners? = null

    val amount_: MutableLiveData<Double> = MutableLiveData()
    val paymentStatus_: MutableLiveData<String> = MutableLiveData()
    val paymentRef_: MutableLiveData<String> = MutableLiveData()
    val customerID_: MutableLiveData<Int> = MutableLiveData()
    val txRef_: MutableLiveData<String> = MutableLiveData()
    val orderRef_: MutableLiveData<String> = MutableLiveData()
    val charge_: MutableLiveData<Double> = MutableLiveData()
    val deliveryMethod_: MutableLiveData<String> = MutableLiveData()
    val itemIDs_: MutableLiveData<IntArray> = MutableLiveData()
    val location_: MutableLiveData<String> = MutableLiveData()



    /**
     * This function will be used to register a customer
     */
    fun registerCustomer(
        fullName: String,
        telephone: String,
        email: String,
        token: String,
        date: String
    ) {
        viewModelScope.launch {
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && telephone.isNotEmpty()) {
                repository.registerCustomer(fullName, telephone, email, token, date).also {
                    onCustomerSignInResultListeners?.handleSignInResult(it)
                }
            }
        }
    }


    private fun String.isPhoneNumber()  = !TextUtils.isEmpty(this) && android.util.Patterns.PHONE.matcher(this).matches()


    private fun String.isEmailValid() =  !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()


    fun loginCustomer(){
           viewModelScope.launch {
               loading()
               if (emailAddress.isEmailValid() && password.isNotEmpty()) {
                   repository.loginCustomer(emailAddress,password).also {
                       onCustomerSignInResultListeners?.handleSignInResult(it)
                   }
               }else{
                   onCustomerSignInResultListeners?.onError("Invalid email or Missing password ")
               }
           }
       }


        fun signUpCustomer() {
            viewModelScope.launch {
                loading()
               if(telephone.isPhoneNumber()){
                    if(emailAddress.isEmailValid()){
                        if (firstName.isNotEmpty() && lastName.isNotEmpty()  && password.isNotEmpty()) {
                            repository.registerCustomer(
                                "$firstName $lastName",
                                telephone,
                                emailAddress,
                                password,
                                " "
                            ).also {
                                onCustomerSignInResultListeners?.handleSignInResult(it)
                            }
                        } else {
                            onCustomerSignInResultListeners?.onError("All fields are required try again")
                        }
                    }else{
                        onCustomerSignInResultListeners?.onError("Invalid Email Address")
                    }
               }else{
                   onCustomerSignInResultListeners?.onError("Invalid Phone number use eg. 0787292442")
               }
            }
        }


        fun goToSignup(view: View)  = view.context.startSignUpActivity()


        fun goToLogin(view: View)  = view.context.startLoginActivity()


        /**
         * This function will be used to get the local customer data that is saved in the Local database using room
         */
        fun getLocalCustomer() = viewModelScope.launch {
            repository.getLocalCustomer()
        }

        val customerDetails: LiveData<SResult<Customer>> by lazy {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(loading())
                emitSource(repository.getLocalCustomer())
            }
        }


        /**
         * IF the use email is know the user will just get Logged in without thought phone validation
         */
        fun isRegisteredCustomer(email: String): LiveData<SResult<Customer>> {
            val isRegistered: LiveData<SResult<Customer>> by lazy {
                liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                    emit(loading())
                    emit(repository.getCustomerDetails(email))
                }
            }

            return isRegistered
        }


        fun signOut() {
            viewModelScope.launch { repository.signOut() }
        }


        fun setMakeOrder(
            amount: Double,
            paymentStatus: String,
            paymentRef: String,
            customerID: Int,
            txRef: String,
            orderRef: String,
            charge: Double,
            deliveryMethod: String,
            itemIDs: IntArray,
            location: String
        ) {
            this.amount_.value = amount
            this.paymentStatus_.value = paymentStatus
            this.paymentRef_.value = paymentRef
            this.customerID_.value = customerID
            this.txRef_.value = txRef
            this.orderRef_.value = orderRef
            this.charge_.value = charge
            this.deliveryMethod_.value = deliveryMethod
            this.itemIDs_.value = itemIDs
            this.location_.value = location

        }

        suspend fun makeOrder() = repository.makeOrder(
            amount_.value!!,
            paymentStatus_.value!!,
            paymentRef_.value!!,
            customerID_.value!!,
            txRef_.value!!,
            orderRef_.value!!,
            charge_.value!!,
            deliveryMethod_.value!!,
            itemIDs_.value!!,
            location_.value!!
        )


        fun getOrderDetails(customerID: Int): LiveData<SResult<List<Order>>> {
            this.customerID_.value = customerID

            val remoteOrderDetailsResponse: LiveData<SResult<List<Order>>> by lazy {
                liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                    emit(loading())
                    emitSource(repository.getOrderDetails(customerID))
                }
            }

            return remoteOrderDetailsResponse
        }


        fun getOrderDetailsItemDetails(
            customerID: Int,
            paymentID: Int
        ): LiveData<SResult<List<OrderItem>>> {

            val remoteOrderItemDetails: LiveData<SResult<List<OrderItem>>> by lazy {
                liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                    emit(loading())
                    emitSource(repository.getOrderItemDetails(customerID, paymentID))
                }
            }

            return remoteOrderItemDetails
        }


        fun orderByPrescription(
            customerID: Int,
            bitmap: MultipartBody.Part,
            orderRef: String
        ): LiveData<SResult<ApiResponse>> {

            val getOrderByPrescription: LiveData<SResult<ApiResponse>> by lazy {
                liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                    emit(loading())
                    emitSource(repository.uploadPrescription(customerID, bitmap, orderRef))
                }
            }

            return getOrderByPrescription
        }



}