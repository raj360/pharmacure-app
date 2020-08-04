package com.example.online_pharmacy_app.activities.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.base.account.AccountsFragment
import com.example.online_pharmacy_app.activities.base.contact.ContactsFragment
import com.example.online_pharmacy_app.activities.base.home.HomeFragment
import com.example.online_pharmacy_app.activities.base.order.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base_bottom_navigation.*

class BaseBottomNavigationActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var accountsFragment: AccountsFragment
    lateinit var ordersFragment: OrdersFragment
    lateinit var contactsFragment: ContactsFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_bottom_navigation)

        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        homeFragment = HomeFragment()
        accountsFragment = AccountsFragment()
        ordersFragment = OrdersFragment()
        contactsFragment = ContactsFragment()
        openFragment(HomeFragment.newInstance())
    }



    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_orders -> {
                    openFragment(OrdersFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_contact -> {
                    openFragment(ContactsFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_account -> {
                    openFragment(AccountsFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun replaceFragment(ResId: Int, fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(ResId, fragment)
            addToBackStack(null)
            commit()
        }


    private fun openFragment(fragment: Fragment)  =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }

    }


