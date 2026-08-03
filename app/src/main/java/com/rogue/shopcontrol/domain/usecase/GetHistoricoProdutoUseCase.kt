package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow

class GetHistoricoProdutoUseCase(
    private val repository: ProdutoRepository
) {

    operator fun invoke(produtoId: Long): Flow<List<ProdutoCompraHistorico>> =
        repository.getHistoricoProduto(produtoId)

}
