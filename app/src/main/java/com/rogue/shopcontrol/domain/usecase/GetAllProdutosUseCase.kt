package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow

class GetAllProdutosUseCase(
    private val repository: ProdutoRepository
) {

    operator fun invoke(): Flow<List<ProdutoEntity>> =
        repository.getAllProdutos()

}
