package com.pharmacure.online_pharmacy_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pharmacure.online_pharmacy_app.activities.base.BaseBottomNavigationActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, BaseBottomNavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}