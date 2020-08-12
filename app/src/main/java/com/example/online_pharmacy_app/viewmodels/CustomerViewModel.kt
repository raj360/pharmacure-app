package com.example.online_pharmacy_app.viewmodels

import androidx.lifecycle.*
import com.example.online_pharmacy_app.activities.OnCustomerSignInResultListeners
import com.example.online_pharmacy_app.domain.repository.CustomerRepository
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.result.loading
import com.example.online_pharmacy_app.viewobjects.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository):ViewModel() {



    //This listener updates helps to update the PhoneVerification process the user data has been
    //saved to the remote and the local server
     var onCustomerSignInResultListeners:OnCustomerSignInResultListeners? = null



    /**
     * This function will be used to register a customer
     */
    fun registerCustomer(fullName: String,
                         telephone: String,
                         email: String,
                         token: String,
                         date: String){
        viewModelScope.launch {
             repository.registerCustomer(fullName, telephone, email, token, date).also {
                 onCustomerSignInResultListeners?.handleSignInResult(it)
             }
        }
    }


    /**
     * This function will be used to get the local customer data that is saved in the Local database using room
     */
    fun getLocalCustomer() = viewModelScope.launch {
        repository.getLocalCustomer()
    }

     val customerDetails: LiveData<SResult<Customer>> by lazy {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO){
            emit(loading())
            emitSource(repository.getLocalCustomer())
        }
    }



}