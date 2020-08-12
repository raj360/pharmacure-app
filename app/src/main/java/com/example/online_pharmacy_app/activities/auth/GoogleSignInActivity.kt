package com.example.online_pharmacy_app.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.online_pharmacy_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_sign_in.*
import android.util.*
import com.example.online_pharmacy_app.common.*

private const val TAG = "GoogleSignInActivity"

class GoogleSignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 1
    private lateinit var signInClient: GoogleSignInClient
    private lateinit var signInOptions: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)
        auth = FirebaseAuth.getInstance()
        initializeUI()
        setupGoogleLogin()
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
                  Intent(this,PhoneVerificationActivity::class.java).apply {
                        this.putExtra(FULLNAME,account.displayName)
                        this.putExtra(EMAIL,account.email)
                        this.putExtra(PHOTO, account.photoUrl.toString())
                        this.putExtra(TOKEN,account.idToken)
                      this.putExtra(DATE,"2020-01-01")
                        startActivity(this)
                    }
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                Log.e(TAG, "onActivityResult: Exception $e")
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun googleFirebaseAuth(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        Log.e(TAG, "Account info: $acct")
        Log.e(TAG, "Account credentials: $credential")
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(TAG, "googleFirebaseAuth: onSuccess $it")
            } else {
                Log.e(TAG, "googleFirebaseAuth: on failure $it")
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
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
