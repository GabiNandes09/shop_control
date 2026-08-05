package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.repository.CategoriaRepository
import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import com.rogue.shopcontrol.domain.usecase.AddCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.AssignCategoriaToProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasByEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoMonthlyHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentosGastoUseCase
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingUseCase
import com.rogue.shopcontrol.domain.usecase.GetHistoricoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoGastoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutosGastoUseCase
import com.rogue.shopcontrol.domain.usecase.SavePurchaseUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoEstabelecimentoUseCase
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
    single {
        CategoriaRepository(
            get()
        )
    }
    single {
        EstabelecimentoRepository(
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
    factory {
        GetProdutoItensComDataUseCase(
            get()
        )
    }
    factory {
        GetAllProdutosUseCase(
            get()
        )
    }
    factory {
        AssignCategoriaToProdutoUseCase(
            get()
        )
    }
    factory {
        GetCategoriasUseCase(
            get()
        )
    }
    factory {
        AddCategoriaUseCase(
            get()
        )
    }
    factory {
        DeleteCategoriaUseCase(
            get()
        )
    }
    factory {
        GetEstabelecimentosGastoUseCase(
            get()
        )
    }
    factory {
        GetEstabelecimentoByIdUseCase(
            get()
        )
    }
    factory {
        GetEstabelecimentoMonthlyHistoryUseCase(
            get()
        )
    }
    factory {
        UpdateApelidoEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetComprasByEstabelecimentoUseCase(
            get()
        )
    }
}