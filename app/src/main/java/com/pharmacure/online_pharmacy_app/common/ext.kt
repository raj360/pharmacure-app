package com.pharmacure.online_pharmacy_app.common

import android.util.Log
import com.pharmacure.online_pharmacy_app.BuildConfig


inline fun log(lambda: () -> String?) {
    if (BuildConfig.DEBUG) {
        Log.e("----------->", lambda() ?: "")
    }
}
