package com.pharmacure.online_pharmacy_app.di


import com.pharmacure.online_pharmacy_app.domain.repository.CartRepository
import com.pharmacure.online_pharmacy_app.domain.repository.CustomerRepository
import com.pharmacure.online_pharmacy_app.domain.repository.DrugRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


val repositoryModule = DI.Module("repository_module") {
    bind() from singleton { CustomerRepository(instance(), instance()) }
    bind() from singleton { DrugRepository(instance(),instance()) }
    bind() from singleton { CartRepository(instance()) }
}