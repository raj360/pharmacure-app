package com.example.online_pharmacy_app.activities.base.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.base.contact.ContactsFragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        fun newInstance(): HomeFragment = HomeFragment()
    }
}