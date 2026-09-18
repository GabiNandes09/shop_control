package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.model.DivisaoParticipanteInput
import java.time.LocalDate

class SaveFixaOuParceladaUseCase(
    private val repository: CompraRepository
) {

    suspend operator fun invoke(
        compraId: Long,
        tipo: TipoCompra,
        nome: String,
        valor: Double,
        estabelecimentoNome: String,
        estabelecimentoApelido: String?,
        data: LocalDate,
        categoriaId: Long,
        totalParcelas: Int?,
        parcelasJaPagas: Int,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ): Long =
        repository.saveFixaOuParcelada(
            compraId = compraId,
            tipo = tipo,
            nome = nome,
            valor = valor,
            estabelecimentoNome = estabelecimentoNome,
            estabelecimentoApelido = estabelecimentoApelido,
            data = data,
            categoriaId = categoriaId,
            totalParcelas = totalParcelas,
            parcelasJaPagas = parcelasJaPagas,
            participantes = participantes
        )

}
