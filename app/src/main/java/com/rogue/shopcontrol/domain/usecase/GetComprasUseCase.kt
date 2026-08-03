package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetComprasUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(): Flow<List<CompraCompleta>> =
        repository.getCompras()

}
