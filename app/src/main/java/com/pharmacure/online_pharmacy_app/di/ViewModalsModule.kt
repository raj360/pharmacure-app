package com.pharmacure.online_pharmacy_app.di

import com.pharmacure.online_pharmacy_app.viewmodels.CartViewModal
import com.pharmacure.online_pharmacy_app.viewmodels.CustomerViewModel
import com.pharmacure.online_pharmacy_app.viewmodels.DrugViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val viewModelsModule = DI.Module("view_models_module") {
    bind() from provider { CustomerViewModel(instance()) }
    bind() from provider { DrugViewModel(instance()) }
    bind() from provider { CartViewModal(instance()) }
}