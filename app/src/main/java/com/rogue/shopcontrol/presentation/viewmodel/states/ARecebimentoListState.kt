package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.utils.parseDataCompra
import java.time.LocalDate

fun ARecebimentoComDados.isAtrasado(): Boolean =
    !pago && parseDataCompra(dataPrevista)?.isBefore(LocalDate.now()) == true

data class ARecebimentoListState(
    val itens: List<ARecebimentoComDados> = emptyList(),
    val isLoading: Boolean = true,

    val showDialog: Boolean = false,
    val isEdicao: Boolean = false,
    val editingId: Long = 0L,
    val editingPago: Boolean = false,
    val editingTipo: TipoARecebimento = TipoARecebimento.PONTUAL,

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

    val tipo: TipoARecebimento = TipoARecebimento.PONTUAL,
    val totalParcelasInput: String = "",

    val errorRes: Int? = null,
    val showDeleteConfirm: Boolean = false,

    val showPagamentoMesDialog: Boolean = false
)
