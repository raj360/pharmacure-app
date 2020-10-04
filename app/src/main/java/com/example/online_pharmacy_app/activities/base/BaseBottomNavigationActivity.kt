package com.example.online_pharmacy_app.activities.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.example.online_pharmacy_app.activities.OnUpdateCartListener
import com.example.online_pharmacy_app.activities.SearchesFragment
import com.example.online_pharmacy_app.activities.auth.GoogleSignInActivity
import com.example.online_pharmacy_app.activities.base.account.AccountsFragment
import com.example.online_pharmacy_app.activities.base.contact.ContactsFragment
import com.example.online_pharmacy_app.activities.base.home.HomeFragment
import com.example.online_pharmacy_app.activities.base.order.OrdersFragment
import com.example.online_pharmacy_app.activities.fragments.CartViewFragment
import com.example.online_pharmacy_app.common.FRAG_TO_OPEN
import com.example.online_pharmacy_app.common.OPEN_CART_FRAGS
import com.example.online_pharmacy_app.common.log
import com.example.online_pharmacy_app.result.SResult
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import com.example.online_pharmacy_app.viewmodels.CustomerViewModel
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.example.online_pharmacy_app.viewobjects.Cart
import com.example.online_pharmacy_app.viewobjects.Customer
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base_bottom_navigation.*
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class BaseBottomNavigationActivity : AppCompatActivity(), OnCartChangeResultListeners,
    OnUpdateCartListener, KodeinAware, SearchView.OnQueryTextListener {

    private var txtViewCount: TextView? = null

    private var progressBarCart: ProgressBar? = null

    private lateinit var cartViewModal: CartViewModal

    lateinit var homeFragment: HomeFragment

    lateinit var accountsFragment: AccountsFragment

    lateinit var ordersFragment: OrdersFragment

    lateinit var drugViewModel: DrugViewModel


    lateinit var contactsFragment: ContactsFragment

    private var noInternetDialog: NoInternetDialog? = null

    override val kodein: Kodein by closestKodein()

    lateinit var cartViewFragment: CartViewFragment

    private lateinit var customerViewModel: CustomerViewModel

    private val factory: ViewModelFactory by instance()

    val TELEPHONE_SCHEMA = "tel:"
    val PRESERVED_CHARACTER = "+"
    val HK_COUNTRY_CODE = "256"
    val HK_OBSERVATORY_PHONE_NUMBER = "787292442"
    private val ARG_QUERY = "ARG_QUERY"
    private var searchQuery: String? = null
    lateinit var searchesFragment: SearchesFragment
    private var openFragment: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_bottom_navigation)
        setSupportActionBar(toolbar)
        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Initialization of viewmodals
        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)
        homeFragment = HomeFragment()
        accountsFragment = AccountsFragment()
        ordersFragment = OrdersFragment()
        contactsFragment = ContactsFragment()
        cartViewFragment = CartViewFragment()
        searchesFragment = SearchesFragment()

        customerViewModel.customerDetails.observe(this, Observer(::handleCustomerDetails))
        //Initialization of viewmodal listeners
        cartViewModal.onCartChangeResultList = this
        cartViewModal.onCartChangeResultList = this
        homeFragment.updateCartListener = this
        searchesFragment.cartViewModal = cartViewModal
        cartViewFragment.updateCartListener = this
        searchesFragment.updateCartListener = this
        searchesFragment.drugViewModel = drugViewModel



        this.openFragment = intent.getStringExtra(FRAG_TO_OPEN)

        log { "Testing open fragment => $openFragment" }

        showProgressBar()


        Handler().postDelayed({
                if(openFragment == FRAG_TO_OPEN || openFragment== null)  {
                    hideProgressBar()
                    openFragment(homeFragment)
                }

                 if(openFragment == OPEN_CART_FRAGS)  {
                    hideProgressBar()
                    openFragment(cartViewFragment)
                }

        }, 2000)


        searchView.setOnQueryTextListener(this)


        searchView.setOnSearchClickListener {
            openFragment(searchesFragment)

        }

        searchView.setOnCloseListener {
            Intent(this, BaseBottomNavigationActivity::class.java).also {
                startActivity(it)
            }
            true
        }


    }

    private fun showProgressBar() {
        baseActivityProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        baseActivityProgressBar.visibility = View.INVISIBLE
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_orders -> {
                    openFragment(ordersFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_contact -> {
                    openFragment(contactsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_account -> {
                    openFragment(accountsFragment)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_call -> {
                    val phoneCallUri =
                        Uri.parse(TELEPHONE_SCHEMA + PRESERVED_CHARACTER + HK_COUNTRY_CODE + HK_OBSERVATORY_PHONE_NUMBER)
                    val phoneCallIntent = Intent(Intent.ACTION_DIAL).also {
                        it.data = phoneCallUri
                    }
                    startActivity(phoneCallIntent)

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    private fun openFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null)
            commitAllowingStateLoss();
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_and_notification_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)

        val notification: View = menu.findItem(R.id.actionNotifications).actionView

        txtViewCount = notification.findViewById(R.id.txtCount) as TextView

        progressBarCart = notification.findViewById(R.id.progressBarCart) as ProgressBar

        cartViewFragment.progressBarCart = progressBarCart
        homeFragment.progressBarCart = progressBarCart

        txtViewCount?.setOnClickListener {
            openFragment(cartViewFragment)
        }

        notification.setOnClickListener {
            openFragment(cartViewFragment)
        }
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
                noInternetConnectionMessage =
                    "Check your Internet connection and try again." // Optional
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

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> updateHotCount(result.data.map { it.quantity }.sum())
            is SResult.Error -> log { "Some error occurred" }
            is SResult.Empty -> log { "Empty result ..." }
        }
    }

    private fun handleCustomerDetails(result: SResult<Customer>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> {
                log { result.data.toString() }
                searchesFragment.customerId=result.data.customerID
                homeFragment.customerId = result.data.customerID
                cartViewFragment.customerId = result.data.customerID
                cartViewModal.setCustomerId(result.data.customerID)
                cartViewModal.getCartData()
            }
            is SResult.Error -> startActivity(Intent(this, GoogleSignInActivity::class.java))
            is SResult.Empty -> startActivity(Intent(this, GoogleSignInActivity::class.java))
        }
    }

    override fun updateHotCount(count: Int) {
        if (count < 0) return
        runOnUiThread {
            if (count == 0) txtViewCount?.visibility = View.GONE else {
                txtViewCount?.visibility = View.VISIBLE
                txtViewCount?.text = count.toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_QUERY, searchQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(ARG_QUERY)
        searchQuery?.let { search(it) }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        this.searchQuery = newText
        this.searchQuery?.let { search(it) }

        return true
    }




    private fun search(query: String) {
        query.isNotEmpty().also { if(it) drugViewModel.searchDrug(query) }
    }


}


