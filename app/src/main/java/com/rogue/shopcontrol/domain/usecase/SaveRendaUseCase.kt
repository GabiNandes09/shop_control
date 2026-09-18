package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaRepository
import java.time.LocalDate

class SaveRendaUseCase(
    private val repository: RendaRepository
) {

    suspend operator fun invoke(
        rendaId: Long,
        descricao: String,
        valor: Double,
        data: LocalDate,
        categoriaId: Long,
        fonteId: Long,
        recorrente: Boolean
    ): Long =
        repository.saveRenda(
            rendaId = rendaId,
            descricao = descricao,
            valor = valor,
            data = data,
            categoriaId = categoriaId,
            fonteId = fonteId,
            recorrente = recorrente
        )

}
