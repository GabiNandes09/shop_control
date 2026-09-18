package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ARecebimentoRepository

class VerificarEEstenderSeriesARecebimentoUseCase(
    private val repository: ARecebimentoRepository
) {

    suspend operator fun invoke() =
        repository.verificarEEstenderSeries()

}
