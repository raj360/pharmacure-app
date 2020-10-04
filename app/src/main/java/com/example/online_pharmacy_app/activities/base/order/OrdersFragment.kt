package com.example.online_pharmacy_app.activities.base.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.online_pharmacy_app.R
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : Fragment(R.layout.fragment_orders) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewStickerOutOfOrders.visibility=View.VISIBLE
    }
}