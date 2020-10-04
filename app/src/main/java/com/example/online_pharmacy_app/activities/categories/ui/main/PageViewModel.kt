package com.example.online_pharmacy_app.activities.categories.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        TAB_TITLES[it - 1]
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun getIndex() = _index.value
}