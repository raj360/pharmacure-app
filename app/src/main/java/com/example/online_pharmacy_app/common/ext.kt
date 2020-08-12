package com.example.online_pharmacy_app.common

import android.util.Log
import com.example.online_pharmacy_app.BuildConfig


inline fun log(lambda: () -> String?) {
    if (BuildConfig.DEBUG) {
        Log.e("----------->", lambda() ?: "")
    }
}
