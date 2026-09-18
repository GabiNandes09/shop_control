package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.domain.model.DateRange

data class AnaliseState(
    val topProdutos: List<ProdutoGasto> = emptyList(),
    val topEstabelecimentos: List<EstabelecimentoGasto> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val isLoading: Boolean = true
)
