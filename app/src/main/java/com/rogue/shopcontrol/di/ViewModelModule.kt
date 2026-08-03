package com.rogue.shopcontrol.di
import com.rogue.shopcontrol.presentation.viewmodel.HomeViewModel
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
        ProductListViewModel(get())
    }

    viewModel { (produtoId: Long) ->
        ProductDetailViewModel(produtoId, get(), get())
    }

}