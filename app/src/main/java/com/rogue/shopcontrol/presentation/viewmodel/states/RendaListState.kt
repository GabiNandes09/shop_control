package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.model.DateRange
import java.time.LocalDate

data class RendaListState(
    val itens: List<RendaComDados> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val isLoading: Boolean = true,

    val showDialog: Boolean = false,
    val isEdicao: Boolean = false,
    val editingId: Long = 0L,

    val descricao: String = "",
    val valorInput: String = "",
    val dataSelecionada: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false,

    val categorias: List<RendaCategoriaEntity> = emptyList(),
    val selectedCategoriaId: Long? = null,
    val showCategoriaPicker: Boolean = false,

    val fonteInput: String = "",
    val fontesSugeridas: List<FonteRendaEntity> = emptyList(),
    val selectedFonteId: Long? = null,

    val recorrente: Boolean = false,

    val errorRes: Int? = null,
    val showDeleteConfirm: Boolean = false
) {

    val totalPeriodo: Double
        get() = itens.sumOf { it.valor }

}
