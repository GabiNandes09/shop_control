package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.repository.ProdutoRepository

class CreateProdutoUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(nome: String): Long =
        repository.insert(
            ProdutoEntity(nome = nome.trim())
        )

}
