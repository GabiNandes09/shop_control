package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.repository.CompraRepository

class GetParcelasDaSerieUseCase(
    private val repository: CompraRepository
) {

    suspend operator fun invoke(compraId: Long): List<CompraEntity> =
        repository.getParcelasDaSerie(compraId)

}
