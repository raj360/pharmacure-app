package com.example.online_pharmacy_app.common

import android.content.Context
import android.content.Intent
import com.example.online_pharmacy_app.activities.DrugDetailsActivity
import com.example.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.example.online_pharmacy_app.activities.base.BaseBottomNavigationActivity


const val FULLNAME = "NAME"
const val EMAIL = "EMAIL"
const val PHOTO = "PHOTO"
const val TOKEN = "TOKEN"
const val DATE = "DATE"

//const val SERVER_URL = "http://192.168.43.36:3000/"
//const val SERVER_URL = "http://10.0.2.2:3000/"
const val SERVER_URL = "https://pharmacure-api.herokuapp.com/"

//Extra Constants
const val CATEGORY = "CATEGORY"
const val DRUG_ID = "DRUG_ID"
const val CUSTOMER_ID = "CUSTOMER_ID"
const val FRAG_TO_OPEN = "FRAG_TO_OPEN"

//Fragments
const val OPEN_CART_FRAGS = "CART_FRAGS"



fun Context.startHomeActivity(fragment: String) =
    Intent(this, BaseBottomNavigationActivity::class.java).also {
        it.putExtra(FRAG_TO_OPEN, fragment)
        startActivity(it)
    }


fun Context.startDrugDetailsPage(drugID: Int, customerID: Int) =
    Intent(this, DrugDetailsActivity::class.java).also {
        it.putExtra(DRUG_ID, drugID)
        it.putExtra(CUSTOMER_ID, customerID)
        startActivity(it)
    }


fun Context.startLoginActivity() =
    Intent(this, GoogleSignInActivity::class.java).also {
        startActivity(it)
    }