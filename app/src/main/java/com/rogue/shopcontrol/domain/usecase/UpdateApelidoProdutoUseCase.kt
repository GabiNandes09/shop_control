package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ProdutoRepository

class UpdateApelidoProdutoUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(produtoId: Long, apelido: String?) =
        repository.updateApelido(produtoId, apelido)

}
