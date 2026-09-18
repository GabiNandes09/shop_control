package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository

class DeleteCompraForwardUseCase(
    private val repository: CompraRepository
) {

    suspend operator fun invoke(compraId: Long) =
        repository.deleteCompraForward(compraId)

}
