package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.usecase.SavePurchaseUseCase
import org.koin.dsl.module

val appModule = module {
    single {
        CompraRepository(
            get(),
            get(),
            get()
        )
    }
    factory {
        SavePurchaseUseCase(
            get()
        )
    }
}