package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.presentation.model.ChartEntry

data class EstablishmentDetailState(
    val estabelecimento: EstabelecimentoEntity? = null,
    val chartEntries: List<ChartEntry> = emptyList(),
    val compras: List<CompraCompleta> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val showEditDialog: Boolean = false,
    val editApelidoText: String = "",
    val isLoading: Boolean = true,
    val estabelecimentosComCnpj: List<EstabelecimentoEntity> = emptyList(),
    val showLinkPicker: Boolean = false,
    val showLinkConfirm: Boolean = false,
    val pendingLinkTargetId: Long? = null,
    val isLinked: Boolean = false,

    val categorias: List<CategoriaEntity> = emptyList(),
    val showCategoryPicker: Boolean = false,

    val showCategoriaConflictDialog: Boolean = false,
    val categoriaConflictOrigemId: Long? = null,
    val categoriaConflictDestinoId: Long? = null,
    val categoriaEscolhidaParaMerge: Long? = null
) {

    val totalGasto: Double
        get() = chartEntries.sumOf { it.value }

    val categoriaNome: String?
        get() = categorias.firstOrNull { it.id == estabelecimento?.categoriaId }?.nome

}
