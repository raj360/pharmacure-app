package com.example.online_pharmacy_app.activities.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.OnCustomerSignInResultListeners
import com.example.online_pharmacy_app.common.*
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CustomerViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Customer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_verification.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

private const val TAG = "PhoneVerification"

class PhoneVerificationActivity : AppCompatActivity(), KodeinAware,
    OnCustomerSignInResultListeners {

    override val kodein: Kodein by closestKodein()
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel

    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var contextView: View
    private lateinit var mVerificationId: String
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mAuth: FirebaseAuth

    private var liveFullName: MutableLiveData<String> = MutableLiveData()
    private var liveTelephone: MutableLiveData<String> = MutableLiveData()
    private var liveEmail: MutableLiveData<String> = MutableLiveData()
    private var liveToken: MutableLiveData<String> = MutableLiveData()
    private var liveDate: MutableLiveData<String> = MutableLiveData()
    private var livePhoneNumber = MutableLiveData<String>()
    private var liveCode = MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)
        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)

        customerViewModel.onCustomerSignInResultListeners = this

        contextView = findViewById<View>(R.id.constraintLayoutVerifications)
        mAuth = FirebaseAuth.getInstance()


        intent.apply {
            liveFullName.value = getStringExtra(FULLNAME)
            displayName.text = getStringExtra(FULLNAME)
            email.text = getStringExtra(EMAIL)
            liveEmail.value = getStringExtra(EMAIL)
            liveToken.value = getStringExtra(PHOTO)
            log { "Live Date =>  ${getStringExtra(DATE)}" }
            liveDate.value = getStringExtra(DATE)

            Glide.with(applicationContext).load(getStringExtra(PHOTO))
                .apply(RequestOptions.circleCropTransform()).into(photoImage)
        }

        sendCode.setOnClickListener {
            editTextCode.text.toString().let {
                if (it.length > 5) {
                    val credential = PhoneAuthProvider.getCredential(mVerificationId, it)
                    signInWithPhoneAuthCredential(credential)
                } else {
                    showSnackBar("Code Incomplete")
                }
            }

        }

        verifyPhoneNumber.setOnClickListener {
            ccp.apply {
                val telephone = "+" + this.selectedCountryCode + editTextPhoneNumber.text.toString()
                livePhoneNumber.value = telephone
                liveTelephone.value = telephone
                startPhoneNumberVerification(telephone)

            }
        }


        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed
                signInWithPhoneAuthCredential(credential)
                showSnackBar("onVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.
                showSnackBar("onVerificationFailed")

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "onVerificationFailed: ${e.message}")
                    showSnackBar("Invalid phone number.")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showSnackBar("Quota exceeded.")
                    Log.e(TAG, "onVerificationFailed: ${e.message}")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                showSnackBar("Code has been sent to you phone")
                liveCode.value = verificationId

                constraintLayoutEntries.visibility = View.GONE
                verifyPhoneNumber.visibility = View.GONE

                sendCode.visibility = View.VISIBLE
                editTextCode.visibility = View.VISIBLE

                //Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
                resendVerificationCode(livePhoneNumber.value!!, mResendToken)
            }
        }

    }


    /**
     * This application will start the verification processd
     */
    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60,             // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            this,           // Activity (for callback binding)
            mCallbacks
        )        // OnVerificationStateChangedCallbacks
    }


    /**
     * This function will be used to pop a snackBar will be used to
     */
    private fun showSnackBar(message: String) =
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show()


    /**
     * This function will be used to to initiate the initial Authentication process
     * using the credential that is generated by the user's code and the token
     */
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    showSnackBar("signInWithCredential:success")
                    customerViewModel.registerCustomer(
                        liveFullName.value!!,
                        liveTelephone.value!!,
                        liveEmail.value!!,
                        liveToken.value!!,
                        liveDate.value!!
                    )

                    log { "Checking for date=> ${liveDate.value}" }
                } else {
                    // Sign in failed, display a message and update the UI
                    showSnackBar("signInWithCredential:failure")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        showSnackBar("Invalid code was entered Try Again")
                        // Save verification ID and resending token so we can use them later
                    }
                    // Sign in failed
                }
            }
    }


    /**
     * This is Resend a verification Code to the user  in
     * case of any errors during authentication
     */
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mCallbacks, // OnVerificationStateChangedCallbacks
            token
        )             // ForceResendingToken from callbacks
    }


    /**
     * The method handles the result after signIn process
     */
    override fun handleSignInResult(result: SResult<Customer>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> finish()
            is SResult.Error -> log { "Some error occurred" }
            is SResult.Empty -> log { "Empty result" }
        }
    }

}