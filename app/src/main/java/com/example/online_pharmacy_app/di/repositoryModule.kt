package com.example.online_pharmacy_app.di


import com.example.online_pharmacy_app.domain.repository.CustomerRepository
import com.example.online_pharmacy_app.viewobjects.Customer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("repository_module"){
    bind() from singleton { CustomerRepository(instance(),instance()) }

}