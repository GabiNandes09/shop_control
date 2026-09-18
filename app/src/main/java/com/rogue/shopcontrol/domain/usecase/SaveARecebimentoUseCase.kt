package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.data.repository.ARecebimentoRepository
import java.time.LocalDate

class SaveARecebimentoUseCase(
    private val repository: ARecebimentoRepository
) {

    suspend operator fun invoke(
        arecebimentoId: Long,
        tipo: TipoARecebimento,
        descricao: String,
        valor: Double,
        dataPrevista: LocalDate,
        categoriaId: Long,
        fonteId: Long,
        totalParcelas: Int?
    ): Long =

        repository.saveARecebimento(
            arecebimentoId = arecebimentoId,
            tipo = tipo,
            descricao = descricao,
            valor = valor,
            dataPrevista = dataPrevista,
            categoriaId = categoriaId,
            fonteId = fonteId,
            totalParcelas = totalParcelas
        )

}
