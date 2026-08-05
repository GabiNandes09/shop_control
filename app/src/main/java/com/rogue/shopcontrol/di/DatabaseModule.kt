package com.rogue.shopcontrol.di

import androidx.room.Room
import com.rogue.shopcontrol.data.local.database.AppDatabase
import com.rogue.shopcontrol.data.local.database.MIGRATION_1_2
import com.rogue.shopcontrol.data.local.database.MIGRATION_2_3
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "shop_control.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
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

    single {
        get<AppDatabase>().categoriaDao()
    }

}