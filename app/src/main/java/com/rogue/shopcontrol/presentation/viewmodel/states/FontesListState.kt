package com.rogue.shopcontrol.presentation.viewmodel.states

data class FonteRendaTotal(
    val id: Long,
    val nome: String,
    val valorTotalRecebido: Double
)

data class FontesListState(
    val fontes: List<FonteRendaTotal> = emptyList(),
    val nameFilter: String = "",
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val newNome: String = "",
    val errorRes: Int? = null
)
