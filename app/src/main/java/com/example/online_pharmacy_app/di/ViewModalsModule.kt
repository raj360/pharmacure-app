package com.example.online_pharmacy_app.di

import com.example.online_pharmacy_app.viewmodels.CustomerViewModel
import com.example.online_pharmacy_app.viewmodels.DrugViewModel
import com.example.online_pharmacy_app.viewobjects.Customer
import com.example.online_pharmacy_app.viewobjects.Drug
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelsModule = Kodein.Module("view_models_module") {
    bind() from provider { CustomerViewModel(instance()) }
    bind() from provider { DrugViewModel(instance()) }
}