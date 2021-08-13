package com.pharmacure.online_pharmacy_app.activities.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.pharmacure.online_pharmacy_app.R
import com.pharmacure.online_pharmacy_app.activities.OnCartChangeResultListeners
import com.pharmacure.online_pharmacy_app.activities.OnShowSnackBarListeners
import com.pharmacure.online_pharmacy_app.activities.OnUpdateCartListener
import com.pharmacure.online_pharmacy_app.activities.SearchesFragment
import com.pharmacure.online_pharmacy_app.activities.base.account.AccountsFragment
import com.pharmacure.online_pharmacy_app.activities.base.home.HomeFragment
import com.pharmacure.online_pharmacy_app.activities.base.order.OrdersFragment
import com.pharmacure.online_pharmacy_app.activities.fragments.CartViewFragment
import com.pharmacure.online_pharmacy_app.common.*
import com.pharmacure.online_pharmacy_app.result.SResult
import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.DrugViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.pharmacure.online_pharmacy_app.viewobjects.Cart
import com.pharmacure.online_pharmacy_app.viewobjects.Customer
import kotlinx.android.synthetic.main.activity_base_bottom_navigation.*
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetDialog
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class BaseBottomNavigationActivity : AppCompatActivity(),
    OnCartChangeResultListeners,
    OnUpdateCartListener,
    DIAware,
    SearchView.OnQueryTextListener,
    OnShowSnackBarListeners
{
    
    private var txtViewCount: TextView? = null

    private var cartImage: ImageView? = null

    private var progressBarCart: ProgressBar? = null

    private lateinit var cartViewModal: CartViewModal


    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }

    private val accountsFragment: AccountsFragment by lazy {
        AccountsFragment.getInstance()
    }

    private val ordersFragment: OrdersFragment by lazy {
        OrdersFragment.getInstance()
    }

    private val cartViewFragment: CartViewFragment by lazy {
        CartViewFragment.getInstance()
    }
    private val searchesFragment: SearchesFragment by lazy {
        SearchesFragment.getInstance()
    }


    lateinit var drugViewModel: DrugViewModel

    private var noInternetDialog: NoInternetDialog? = null

    override val di by di(this)
    private val factory: ViewModelFactory by instance()
    private lateinit var customerViewModel: CustomerViewModel

    private lateinit var onShowSnackBarListeners: OnShowSnackBarListeners





    val TELEPHONE_SCHEMA = "tel:"
    val PRESERVED_CHARACTER = "+"
    val HK_COUNTRY_CODE = "256"
    val HK_OBSERVATORY_PHONE_NUMBER = "7000000000"

    private val ARG_QUERY = "ARG_QUERY"
    private var searchQuery: String? = null

    private var openFragment: String? = null

    /**
     * Test customer
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_bottom_navigation)
        setSupportActionBar(toolbar)
        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Initialization of ViewModels
        drugViewModel = ViewModelProvider(this, factory).get(DrugViewModel::class.java)
        customerViewModel = ViewModelProvider(this, factory).get(CustomerViewModel::class.java)
        cartViewModal = ViewModelProvider(this, factory).get(CartViewModal::class.java)


        customerViewModel.customerDetails.observe(this, Observer(::handleCustomerDetails))
        //Initialization of ViewModel listeners
        cartViewModal.onCartChangeResultList = this
        cartViewModal.onCartChangeResultList = this
        homeFragment.updateCartListener = this
        searchesFragment.cartViewModal = cartViewModal
        cartViewFragment.updateCartListener = this
        searchesFragment.updateCartListener = this
        searchesFragment.drugViewModel = drugViewModel
        cartViewFragment.customerViewModel = customerViewModel
        ordersFragment.customerViewModel = customerViewModel

        //Initialization of the showSnackListener
        onShowSnackBarListeners = this
        homeFragment.onShowSnackBarListeners = onShowSnackBarListeners
        cartViewFragment.onShowSnackBarListeners = onShowSnackBarListeners
        ordersFragment.onShowSnackBarListeners = onShowSnackBarListeners



        this.openFragment = intent.getStringExtra(FRAG_TO_OPEN)
        showProgressBar()



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

        initializeFragments(openFragment)


    }


    private fun initializeFragments(openFragment: String?) {

        Handler().postDelayed({
            when (openFragment) {
                OPEN_CART_FRAGS -> {
                    hideProgressBar()
                    openFragment(cartViewFragment)
                }
                OPEN_ORDERS_FRAG -> {
                    hideProgressBar()
                    openFragment(ordersFragment)
                    bottomBar.menu.getItem(2).isChecked = true
                }
                else -> {
                    hideProgressBar()
                    openFragment(homeFragment)
                    bottomBar.menu.getItem(0).isChecked = true
                }
            }

        }, 1000)
    }

    /**
    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
    navigationView.getMenu().getItem(2).setChecked(true);
     */

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
            commitAllowingStateLoss()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_and_notification_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)

        val notification: View = menu.findItem(R.id.actionNotifications).actionView

        txtViewCount = notification.findViewById(R.id.txtCount) as TextView
        progressBarCart = notification.findViewById(R.id.progressBarCart) as ProgressBar
        cartImage = notification.findViewById(R.id.cartIconHome) as ImageView


        txtViewCount?.setOnClickListener {
            openFragment(cartViewFragment)
        }

        cartImage?.setOnClickListener { openFragment(cartViewFragment) }

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

        this.openFragment = intent.getStringExtra(FRAG_TO_OPEN)
        initializeFragments(openFragment)
    }


    override fun onPause() {
        super.onPause()
        noInternetDialog?.destroy()
    }

    override fun handleCartResult(result: SResult<List<Cart>>) {
        when (result) {
            is SResult.Loading -> {
                showCartProgress()
            }

            is SResult.Success -> {
                hideCartProgress()
                updateHotCount(result.data.map { it.quantity }.sum())
            }
            is SResult.Error -> {
                hideCartProgress()
                showSnackBack("Error occurred ${result.message}")
            }
            is SResult.Empty -> {
                updateHotCount(0)
                hideCartProgress()
                showSnackBack("No response from server")
            }
        }
    }

    private fun handleCustomerDetails(result: SResult<Customer>) {
        when (result) {
            is SResult.Loading -> log { "Loading..." }
            is SResult.Success -> {
                accountsFragment.customer = result.data
                searchesFragment.customerId = result.data.customerID
                homeFragment.customerId = result.data.customerID
                cartViewFragment.customerId = result.data.customerID
                cartViewFragment.customer = result.data
                ordersFragment.customerId = result.data.customerID
                cartViewModal.setCustomerId(result.data.customerID)
                cartViewModal.getCartData()
            }
            is SResult.Error -> this.startLoginActivity()
            is SResult.Empty -> this.startLoginActivity()
        }
    }

    override fun updateHotCount(count: Int) {
        if (count < 0) return
        runOnUiThread {
            if (count == 0) txtViewCount?.visibility = View.GONE
            else {
                txtViewCount?.visibility = View.VISIBLE
                txtViewCount?.text = count.toString()
            }
        }
    }

    override fun showCartProgress() {
        progressBarCart?.visibility = View.VISIBLE
        cartImage?.visibility = View.GONE
        txtViewCount?.visibility = View.GONE
    }


    override fun hideCartProgress() {
        progressBarCart?.visibility = View.GONE
        cartImage?.visibility = View.VISIBLE
        txtViewCount?.visibility = View.VISIBLE
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
        query.isNotEmpty().also { if (it) drugViewModel.searchDrug(query) }
    }

    override fun showSnackBack(message: String) {
        Snackbar.make(baseActivityCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
    }


}












