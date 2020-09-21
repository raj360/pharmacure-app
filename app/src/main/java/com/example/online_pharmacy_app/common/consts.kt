package com.example.online_pharmacy_app.common

import android.content.Context
import android.content.Intent
import com.example.online_pharmacy_app.activities.DrugDetailsActivity
import com.example.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.example.online_pharmacy_app.activities.base.BaseBottomNavigationActivity
import com.example.online_pharmacy_app.activities.fragments.DrugDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn

const val FULLNAME = "NAME"
const val EMAIL = "EMAIL"
const val PHOTO = "PHOTO"
const val TOKEN = "TOKEN"
const val DATE = "DATE"

//const val SERVER_URL = "http://192.168.43.36:3000/"
const val SERVER_URL = "http://10.0.2.2:3000/"
//const val SERVER_URL = "https://pharmacure-api.herokuapp.com/"


const val CATEGORY = "CATEGORY"
const val DRUG_ID = "DRUG_ID"


fun Context.startHomeActivity() =
    Intent(this, BaseBottomNavigationActivity::class.java).also {
        startActivity(it)
    }


fun Context.startDrugDetailsPage(drugID: Int) =
    Intent(this, DrugDetailsActivity::class.java).also {
        it.putExtra(DRUG_ID, drugID)
        startActivity(it)
    }


fun Context.startLoginActivity() =
    Intent(this, GoogleSignInActivity::class.java).also {
        startActivity(it)
    }