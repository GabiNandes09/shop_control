package com.rogue.shopcontrol

import android.app.Application
import com.rogue.shopcontrol.di.appModule
import com.rogue.shopcontrol.di.databaseModule
import com.rogue.shopcontrol.di.networkModule
import com.rogue.shopcontrol.di.parseModule
import com.rogue.shopcontrol.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule,
                networkModule,
                databaseModule,
                viewModelModule,
                parseModule
            )
        }
    }
}