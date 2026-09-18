package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ProdutoRepository

class MergeProdutosUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(origemId: Long, destinoId: Long) =
        repository.mergeProdutos(origemId, destinoId)

}
