package com.rogue.shopcontrol.di
import com.rogue.shopcontrol.presentation.viewmodel.PurchaseDetailViewModel
import com.rogue.shopcontrol.presentation.viewmodel.RecordsViewModel
import com.rogue.shopcontrol.presentation.viewmodel.ScannerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel {
        ScannerViewModel(get(), get(), get())
    }

    viewModel {
        RecordsViewModel(get())
    }

    viewModel { (compraId: Long) ->
        PurchaseDetailViewModel(compraId, get(), get())
    }

}