package com.rogue.shopcontrol.di
import com.rogue.shopcontrol.presentation.viewmodel.ARecebimentoListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.AnaliseViewModel
import com.rogue.shopcontrol.presentation.viewmodel.BalancoViewModel
import com.rogue.shopcontrol.presentation.viewmodel.CategoryManagementViewModel
import com.rogue.shopcontrol.presentation.viewmodel.CategorySpendingViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ComprasParceladasViewModel
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.FonteDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.FontesListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.HomeViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ManualPurchaseEntryViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductCatalogViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.PurchaseDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.RecordsViewModel
import com.rogue.shopcontrol.presentation.viewmodel.RendaCategoriaManagementViewModel
import com.rogue.shopcontrol.presentation.viewmodel.RendaListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ScannerViewModel
import com.rogue.shopcontrol.presentation.viewmodel.SettingsViewModel
import com.rogue.shopcontrol.presentation.viewmodel.SpendingComparisonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        AnaliseViewModel(get(), get(), get())
    }

    viewModel {
        ScannerViewModel(get(), get(), get())
    }

    viewModel {
        RecordsViewModel(get(), get())
    }

    viewModel { (compraId: Long) ->
        PurchaseDetailViewModel(compraId, get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        SpendingComparisonViewModel(get())
    }

    viewModel {
        ProductListViewModel(get(), get(), get())
    }

    viewModel { (produtoId: Long) ->
        ProductDetailViewModel(produtoId, get(), get(), get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        ProductCatalogViewModel(get())
    }

    viewModel {
        CategoryManagementViewModel(get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        EstablishmentListViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel { (estabelecimentoId: Long) ->
        EstablishmentDetailViewModel(estabelecimentoId, get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        CategorySpendingViewModel(get(), get(), get())
    }

    viewModel { (compraId: Long) ->
        ManualPurchaseEntryViewModel(compraId, get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        RendaListViewModel(get(), get(), get(), get(), get(), get(), get())
    }

    viewModel {
        FontesListViewModel(get(), get(), get())
    }

    viewModel { (fonteId: Long) ->
        FonteDetailViewModel(fonteId, get(), get())
    }

    viewModel {
        RendaCategoriaManagementViewModel(get(), get(), get(), get())
    }

    viewModel {
        ComprasParceladasViewModel(get(), get())
    }

    viewModel {
        BalancoViewModel(get())
    }

    viewModel { (highlightId: Long) ->
        ARecebimentoListViewModel(highlightId, get(), get(), get(), get(), get(), get(), get())
    }

}