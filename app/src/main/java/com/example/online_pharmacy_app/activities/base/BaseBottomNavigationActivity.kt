package com.example.online_pharmacy_app.activities.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.base.account.AccountsFragment
import com.example.online_pharmacy_app.activities.base.contact.ContactsFragment
import com.example.online_pharmacy_app.activities.base.home.HomeFragment
import com.example.online_pharmacy_app.activities.base.order.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base_bottom_navigation.*
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog

class BaseBottomNavigationActivity : AppCompatActivity() {
    private  var txtViewCount: TextView? = null
    lateinit var homeFragment: HomeFragment
    lateinit var accountsFragment: AccountsFragment
    lateinit var ordersFragment: OrdersFragment
    lateinit var contactsFragment: ContactsFragment
    private var noInternetDialog: NoInternetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_bottom_navigation)
        setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_and_notification_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)

        val notification: View = menu.findItem(R.id.actionNotifications).actionView

        txtViewCount = notification.findViewById(R.id.txtCount) as TextView

//        txtViewCount?.setOnClickListener {
//            replaceFragment(R.id.fl_bottom_nav,cartViewFragment)
//        }

//        notification.setOnClickListener {
//            replaceFragment(R.id.fl_bottom_nav,cartViewFragment)
//        }
        return true
    }

    override fun onResume() {
        super.onResume()
        noInternetDialog = NoInternetDialog.Builder(this)
            .apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        return
                    }
                }
                cancelable = true // Optional
                noInternetConnectionTitle = "No Internet" // Optional
                noInternetConnectionMessage = "Check your Internet connection and try again." // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = "Please turn on" // Optional
                wifiOnButtonText = "Wifi" // Optional
                mobileDataOnButtonText = "Mobile data" // Optional
                onAirplaneModeTitle = "No Internet" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                pleaseTurnOffText = "Please turn off" // Optional
                airplaneModeOffButtonText = "Airplane mode" // Optional
                showAirplaneModeOffButtons = true // Optional
            }
            .build()
    }


    override fun onPause() {
        super.onPause()
        noInternetDialog?.destroy()
    }



}


