package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaCategoriaRepository

class DeleteRendaCategoriaUseCase(
    private val repository: RendaCategoriaRepository
) {

    suspend operator fun invoke(categoriaId: Long): Boolean =
        repository.deleteCategoria(categoriaId)

}
