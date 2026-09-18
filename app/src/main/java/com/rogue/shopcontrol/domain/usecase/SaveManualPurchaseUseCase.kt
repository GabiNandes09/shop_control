package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.model.DivisaoParticipanteInput
import com.rogue.shopcontrol.domain.model.ManualCompraItemInput

class SaveManualPurchaseUseCase(
    private val repository: CompraRepository
) {

    suspend operator fun invoke(
        compraId: Long,
        estabelecimentoNome: String,
        estabelecimentoApelido: String?,
        dataCompra: String?,
        categoriaId: Long?,
        itens: List<ManualCompraItemInput>,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ): Long =
        repository.saveManual(
            compraId = compraId,
            estabelecimentoNome = estabelecimentoNome,
            estabelecimentoApelido = estabelecimentoApelido,
            dataCompra = dataCompra,
            categoriaId = categoriaId,
            itens = itens,
            participantes = participantes
        )

}
