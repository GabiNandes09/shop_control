package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow

class GetProdutoGastoByIdUseCase(
    private val repository: ProdutoRepository
) {

    operator fun invoke(produtoId: Long): Flow<ProdutoGasto?> =
        repository.getProdutoGastoById(produtoId)

}
