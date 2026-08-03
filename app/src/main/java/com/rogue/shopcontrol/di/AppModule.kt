package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingUseCase
import com.rogue.shopcontrol.domain.usecase.GetHistoricoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoGastoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutosGastoUseCase
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
    single {
        ProdutoRepository(
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
    factory {
        GetMonthlySpendingUseCase(
            get()
        )
    }
    factory {
        GetMonthlySpendingHistoryUseCase(
            get()
        )
    }
    factory {
        GetProdutosGastoUseCase(
            get()
        )
    }
    factory {
        GetProdutoGastoByIdUseCase(
            get()
        )
    }
    factory {
        GetHistoricoProdutoUseCase(
            get()
        )
    }
}