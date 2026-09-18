package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ProdutoRepository

class UpdateCodigoBarrasProdutoUseCase(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(produtoId: Long, codigoBarras: String?) =
        repository.updateCodigoBarras(produtoId, codigoBarras)

}
