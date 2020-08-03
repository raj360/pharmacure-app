package com.example.online_pharmacy_app.activities.base.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.base.contact.ContactsFragment


class OrdersFragment : Fragment(R.layout.fragment_orders) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): OrdersFragment = OrdersFragment()
    }
}