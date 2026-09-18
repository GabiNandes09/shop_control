package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository

class VerificarEEstenderSeriesFixaUseCase(
    private val repository: CompraRepository
) {

    suspend operator fun invoke() =
        repository.verificarEEstenderSeriesFixa()

}
