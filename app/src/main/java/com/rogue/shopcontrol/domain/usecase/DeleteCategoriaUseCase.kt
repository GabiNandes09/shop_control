package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CategoriaRepository

class DeleteCategoriaUseCase(
    private val repository: CategoriaRepository
) {

    suspend operator fun invoke(categoriaId: Long) =
        repository.deleteCategoria(categoriaId)

}
