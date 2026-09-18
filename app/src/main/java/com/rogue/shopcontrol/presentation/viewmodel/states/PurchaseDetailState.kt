package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.DivisaoParticipanteComNome
import com.rogue.shopcontrol.presentation.model.PriceAlert

data class PurchaseDetailState(
    val compra: CompraCompleta? = null,
    val precoAlerts: Map<Long, PriceAlert> = emptyMap(),
    val parcelas: List<CompraEntity> = emptyList(),
    val categoriaNome: String? = null,
    val divisaoParticipantes: List<DivisaoParticipanteComNome> = emptyList(),
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)
