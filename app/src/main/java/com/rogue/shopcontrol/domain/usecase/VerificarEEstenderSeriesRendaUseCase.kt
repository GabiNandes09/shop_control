package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaRepository

class VerificarEEstenderSeriesRendaUseCase(
    private val repository: RendaRepository
) {

    suspend operator fun invoke() =
        repository.verificarEEstenderSeries()

}
