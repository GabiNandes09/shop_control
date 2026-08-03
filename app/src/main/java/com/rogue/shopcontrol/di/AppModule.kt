package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
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
    factory {
        GetComprasUseCase(
            get()
        )
    }
    factory {
        GetCompraByIdUseCase(
            get()
        )
    }
    factory {
        DeleteCompraUseCase(
            get()
        )
    }
}