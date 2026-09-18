package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ProdutoRepository

class LinkProdutosToCategoriaUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(
        categoriaId: Long,
        produtoIdsParaVincular: List<Long>,
        produtoIdsParaDesvincular: List<Long>
    ) =
        repository.linkProdutosToCategoria(categoriaId, produtoIdsParaVincular, produtoIdsParaDesvincular)

}
