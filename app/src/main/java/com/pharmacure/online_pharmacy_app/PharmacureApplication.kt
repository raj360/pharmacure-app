package com.pharmacure.online_pharmacy_app

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.FirebaseApp
import com.pharmacure.online_pharmacy_app.di.*
import com.pharmacure.online_pharmacy_app.providers.database.PharmacureDatabase
import com.pharmacure.online_pharmacy_app.viewmodels.factory.ViewModelFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.singleton


class PharmacureApplication : Application(), DIAware {

    override val di: DI by DI.lazy {
        import(androidXModule(this@PharmacureApplication))
        bind() from singleton { ViewModelFactory(applicationContext) }
        bind() from singleton {
            Room.databaseBuilder(applicationContext, PharmacureDatabase::class.java, "PharmacureDB")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addCallback(object : RoomDatabase.Callback() { // Add this callback
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("INSERT INTO  DrugFts(DrugFts) VALUES ('rebuild')")
                    }
                })
                .allowMainThreadQueries()
                .build()
        }
        import(networkModule, allowOverride = true)
        import(databaseModule, allowOverride = true)
        import(localDataSourceModule, allowOverride = true)
        import(remoteDataSourceModule, allowOverride = true)
        import(repositoryModule, allowOverride = true)
        import(viewModelsModule, allowOverride = true)
    }


    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Drug` (`drugID` INTEGER NOT NULL PRIMARY KEY, `drugName` TEXT NOT NULL, `drugType` TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT NOT NULL, unitPrice INTEGER NOT NULL, dose TEXT NOT NULL, categoryID INTEGER NOT NULL, caTname TEXT NOT NULL, subCategoryID INTEGER NOT NULL, subName TEXT NOT NULL,inventoryID INTEGER NOT NULL, quantity INTEGER NOT NULL, adminID INTEGER NOT NULL ) ");
        }
    }


    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("INSERT INTO  DrugFts(DrugFts) VALUES ('rebuild')")
        }
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}

