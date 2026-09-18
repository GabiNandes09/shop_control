package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CompraComData
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetComprasComDataUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(): Flow<List<CompraComData>> =
        repository.getComprasComData()

}
