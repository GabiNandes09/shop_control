package com.rogue.shopcontrol.di
import com.rogue.shopcontrol.presentation.viewmodel.CategoryManagementViewModel
import com.rogue.shopcontrol.presentation.viewmodel.CategorySpendingViewModel
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.HomeViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductCatalogViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ProductListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.PurchaseDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.RecordsViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ScannerViewModel
import com.rogue.shopcontrol.presentation.viewmodel.SpendingComparisonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        HomeViewModel(get(), get())
    }

    viewModel {
        ScannerViewModel(get(), get(), get())
    }

    viewModel {
        RecordsViewModel(get())
    }

    viewModel { (compraId: Long) ->
        PurchaseDetailViewModel(compraId, get(), get())
    }

    viewModel {
        SpendingComparisonViewModel(get())
    }

    viewModel {
        ProductListViewModel(get(), get())
    }

    viewModel { (produtoId: Long) ->
        ProductDetailViewModel(produtoId, get(), get(), get(), get())
    }

    viewModel {
        ProductCatalogViewModel(get())
    }

    viewModel {
        CategoryManagementViewModel(get(), get(), get(), get())
    }

    viewModel {
        EstablishmentListViewModel(get())
    }

    viewModel { (estabelecimentoId: Long) ->
        EstablishmentDetailViewModel(estabelecimentoId, get(), get(), get(), get())
    }

    viewModel {
        CategorySpendingViewModel(get())
    }

}