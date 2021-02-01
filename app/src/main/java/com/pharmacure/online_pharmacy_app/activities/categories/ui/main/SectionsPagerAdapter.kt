package com.pharmacure.online_pharmacy_app.activities.categories.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

val TAB_TITLES = arrayOf(
    "Gastro intestinal system",
    "Cardiovascular system",
    "Respiratory system",
    "nervous system",
    "Infection",
    "Endocrine system",
    "Genito urinary system",
    "Immune system and malignant disease",
    "blood and nutrition",
    "Eye",
    "Ear, nose, and or pharynx",
    "skin",
    "musculoskeletal "
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var categoryID: Int = 0
    var customerID: Int = 0

    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(position + 1, categoryID,customerID)
    }

    override fun getPageTitle(position: Int) = TAB_TITLES[position]


    override fun getCount() = TAB_TITLES.size


}


