package com.pharmacure.online_pharmacy_app.activities.auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.OnCustomerSignInResultListeners
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.databinding.ActivityLoginBinding
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class LoginActivity : AppCompatActivity(),DIAware,OnCustomerSignInResultListeners {

    private lateinit var progressDialog: ProgressDialog
    override val di: DI by di(this)
    private val factory:ViewModelFactory by instance()
    private lateinit var binding:ActivityLoginBinding
    private  lateinit var customerViewModel: CustomerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        setSupportActionBar(binding.toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressDialog = ProgressDialog(this)
        customerViewModel= ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        customerViewModel.onCustomerSignInResultListeners =this
        binding.viewModel = customerViewModel

    }

    override fun handleSignInResult(result: SResult<Customer>) {
        when (result) {
            is SResult.Loading -> showProgressDialog(progressDialog,"Logging in")

            is SResult.Success -> {
                if(result.data.email.isNotEmpty()){
                    this.startHomeActivity(FRAG_TO_OPEN)
                }else{
                    Toast.makeText(this,"Wrong email or password, Try again", Toast.LENGTH_SHORT).show()
                }
            }

            is SResult.Error -> Toast.makeText(this,result.message, Toast.LENGTH_SHORT).show()

            is SResult.Empty -> Toast.makeText(this,"No response from server", Toast.LENGTH_SHORT).show()

        }
    }




    override fun onError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}