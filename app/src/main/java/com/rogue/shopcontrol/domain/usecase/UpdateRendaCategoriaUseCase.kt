package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaCategoriaRepository

class UpdateRendaCategoriaUseCase(
    private val repository: RendaCategoriaRepository
) {

    suspend operator fun invoke(categoriaId: Long, nome: String): Boolean =
        repository.updateCategoria(categoriaId, nome)

}
