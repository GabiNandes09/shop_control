package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.domain.model.DateRange

data class EstablishmentListState(
    val estabelecimentos: List<EstabelecimentoGasto> = emptyList(),
    val nameFilter: String = "",
    val dateRange: DateRange = DateRange.currentMonth(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val newNome: String = "",
    val newCnpj: String = "",
    val newEndereco: String = "",
    val newApelido: String = "",
    val addErrorRes: Int? = null,
    val categorias: List<CategoriaEntity> = emptyList(),
    val newCategoriaId: Long? = null,
    val showNewCategoryPicker: Boolean = false
)
