package com.pharmacure.online_pharmacy_app.activities.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdListener
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_sign_in.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class GoogleSignInActivity : AppCompatActivity(), DIAware {

    private val RC_SIGN_IN: Int = 1
    override val di: DI by di(this)

    private val factory: ViewModelFactory by instance()
    private lateinit var signInClient: GoogleSignInClient
    private lateinit var signInOptions: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth
    private lateinit var customerViewModel: CustomerViewModel
    private var accountGlobal: GoogleSignInAccount? = null
    private val TAG = "GoogleSignInActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)
        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        initializeUI()
        setupGoogleLogin()

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        adViewLogin.loadAd(adRequest)
        adViewLogin.visibility = View.GONE
        adViewLogin.loadAd(adRequest)
        adViewLogin.adListener = object: AdListener() {
            override fun onAdLoaded() {
                adViewLogin.visibility = View.GONE

            }
        }




   }

    private fun initializeUI() {
        google_button.setOnClickListener {
            login()
        }
    }


    private fun login() {
        val loginIntent: Intent = signInClient.signInIntent
        startActivityForResult(loginIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun googleFirebaseAuth(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        Log.e(TAG, "Account info: $account")
        Log.e(TAG, "Account credentials: $credential")
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                accountGlobal = account
                customerViewModel.isRegisteredCustomer(account.email!!)
                    .observe(this, Observer(::handleIsRegisteredCustomer))

                Log.e(TAG, "googleFirebaseAuth: onSuccess $it")
            }
        }
    }

    private fun openPhoneValidation(account: GoogleSignInAccount) {
        Intent(this, PhoneVerificationActivity::class.java).apply {
            this.putExtra(FULL_NAME, account.displayName)
            this.putExtra(EMAIL, account.email)
            this.putExtra(PHOTO, account.photoUrl.toString())
            this.putExtra(TOKEN, account.idToken)
            this.putExtra(DATE, "2020-01-01")
            startActivity(this)
        }
    }

    private fun handleIsRegisteredCustomer(result: SResult<Customer>) {
        when (result) {
            is SResult.Success -> {
                result.data.let { customer ->
                    if (customer.email.isNotEmpty()) {
                        this.startHomeActivity(FRAG_TO_OPEN)
                    } else {
                        openPhoneValidation(this.accountGlobal!!)
                    }
                }
            }

            is SResult.Empty -> {
                Toast.makeText(this, "No response from server", Toast.LENGTH_SHORT).show()
            }

            is SResult.Error -> {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun setupGoogleLogin() {
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, signInOptions)
    }
}
