package com.rogue.shopcontrol.di

import androidx.room.Room
import com.rogue.shopcontrol.data.local.database.AppDatabase
import com.rogue.shopcontrol.data.local.database.MIGRATION_1_2
import com.rogue.shopcontrol.data.local.database.MIGRATION_2_3
import com.rogue.shopcontrol.data.local.database.MIGRATION_3_4
import com.rogue.shopcontrol.data.local.database.MIGRATION_4_5
import com.rogue.shopcontrol.data.local.database.MIGRATION_5_6
import com.rogue.shopcontrol.data.local.database.MIGRATION_6_7
import com.rogue.shopcontrol.data.local.database.MIGRATION_7_8
import com.rogue.shopcontrol.data.local.database.MIGRATION_8_9
import com.rogue.shopcontrol.data.local.database.MIGRATION_9_10
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "shop_control.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
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

    single {
        get<AppDatabase>().rendaDao()
    }

    single {
        get<AppDatabase>().rendaCategoriaDao()
    }

    single {
        get<AppDatabase>().fonteRendaDao()
    }

    single {
        get<AppDatabase>().arecebimentoDao()
    }

    single {
        get<AppDatabase>().divisaoContaDao()
    }

}