package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetCompraByIdUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(compraId: Long): Flow<CompraCompleta?> =
        repository.getCompraById(compraId)

}
