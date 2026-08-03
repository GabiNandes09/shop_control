package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow

class GetProdutosGastoUseCase(
    private val repository: ProdutoRepository
) {

    operator fun invoke(): Flow<List<ProdutoGasto>> =
        repository.getProdutosGasto()

}
