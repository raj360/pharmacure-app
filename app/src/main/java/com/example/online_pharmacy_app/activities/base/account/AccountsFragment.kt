package com.example.online_pharmacy_app.activities.base.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.online_pharmacy_app.R

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): AccountsFragment = AccountsFragment()
    }
}