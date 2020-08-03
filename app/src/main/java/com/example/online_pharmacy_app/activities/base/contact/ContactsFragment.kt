package com.example.online_pharmacy_app.activities.base.contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.base.account.AccountsFragment


class ContactsFragment : Fragment(R.layout.fragment_contacts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): ContactsFragment = ContactsFragment()
    }
}