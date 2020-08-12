package com.example.online_pharmacy_app

import android.app.Application
import androidx.room.Room
import com.example.online_pharmacy_app.di.*
import com.example.online_pharmacy_app.providers.database.PharmacureDatabase
import com.example.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import com.google.firebase.FirebaseApp
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class PharmacureApplication:Application() , KodeinAware {
    /**
     * this is dependence injection using kodein this
     * enable us to prevent dependencies against objects
     */
    override val kodein: Kodein by Kodein.lazy {
        bind() from singleton { ViewModelFactory(applicationContext) }
        /**
         * this our offline database
         */
        bind() from singleton {
            Room.databaseBuilder(applicationContext, PharmacureDatabase::class.java, "PharmacureDB")
                .allowMainThreadQueries()
                .build()
        }

        import(androidXModule(this@PharmacureApplication))
        import(networkModule)
        import(databaseModule)
        import(localDataSourceModule)
        import(remoteDataSourceModule)
        import(repositoryModule)
        import(useCasesModule)
        import(viewModelsModule)

    }

        override fun onCreate() {
            super.onCreate()
            FirebaseApp.initializeApp(this);
        }
    }

