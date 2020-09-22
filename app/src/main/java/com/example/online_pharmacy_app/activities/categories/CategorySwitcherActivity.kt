package com.example.online_pharmacy_app.activities.categories

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.online_pharmacy_app.R
import com.example.online_pharmacy_app.activities.categories.ui.main.SectionsPagerAdapter
import com.example.online_pharmacy_app.common.CATEGORY
import kotlinx.android.synthetic.main.activity_category_switcher.*


class CategorySwitcherActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_switcher)
        setSupportActionBar(toolbarCategorySwitcher)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val categoryID = intent.getIntExtra(CATEGORY, 0)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.categoryID=categoryID
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }
}