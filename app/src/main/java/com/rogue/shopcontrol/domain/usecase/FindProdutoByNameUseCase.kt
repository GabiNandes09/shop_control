package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.repository.ProdutoRepository

class FindProdutoByNameUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(nome: String): ProdutoEntity? =
        repository.findByName(nome)

}
