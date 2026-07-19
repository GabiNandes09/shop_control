package com.rogue.shopcontrol.di

import androidx.room.Room
import com.rogue.shopcontrol.data.local.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "shop_control.db"
        ).build()
    }

    single {
        get<AppDatabase>().estabelecimentoDao()
    }

    single {
        get<AppDatabase>().produtoDao()
    }

    single {
        get<AppDatabase>().compraDao()
    }

}