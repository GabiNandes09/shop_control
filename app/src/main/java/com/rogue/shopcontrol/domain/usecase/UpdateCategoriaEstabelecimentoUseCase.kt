package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository

class UpdateCategoriaEstabelecimentoUseCase(
    private val repository: EstabelecimentoRepository
) {

    suspend operator fun invoke(estabelecimentoId: Long, categoriaId: Long?) =
        repository.updateCategoria(estabelecimentoId, categoriaId)

}
