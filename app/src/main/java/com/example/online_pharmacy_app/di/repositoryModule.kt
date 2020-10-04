package com.example.online_pharmacy_app.di


import com.example.online_pharmacy_app.domain.repository.CartRepository
import com.example.online_pharmacy_app.domain.repository.CustomerRepository
import com.example.online_pharmacy_app.domain.repository.DrugRepository
import com.example.online_pharmacy_app.viewmodels.CartViewModal
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("repository_module") {
    bind() from singleton { CustomerRepository(instance(), instance()) }
    bind() from singleton { DrugRepository(instance()) }
    bind() from singleton { CartRepository(instance()) }
    bind() from provider { CartViewModal(instance()) }
}