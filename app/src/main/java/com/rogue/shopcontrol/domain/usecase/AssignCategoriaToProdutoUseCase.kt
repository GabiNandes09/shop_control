package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ProdutoRepository

class AssignCategoriaToProdutoUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(produtoId: Long, categoriaId: Long) =
        repository.assignCategoria(produtoId, categoriaId)

}
