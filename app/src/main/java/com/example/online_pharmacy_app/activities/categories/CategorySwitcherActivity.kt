package com.example.online_pharmacy_app.activities.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.categories.ui.main.SectionsPagerAdapter
import com.example.online_pharmacy_app.common.CATEGORY
import com.example.online_pharmacy_app.common.CUSTOMER_ID
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_category_switcher.*


class CategorySwitcherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_switcher)
        setSupportActionBar(toolbarCategorySwitcher)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Categories"

        val categoryID = intent.getIntExtra(CATEGORY, 0)
        val customerID = intent.getIntExtra(CUSTOMER_ID,0)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.categoryID = categoryID
        sectionsPagerAdapter.customerID =customerID
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }
}