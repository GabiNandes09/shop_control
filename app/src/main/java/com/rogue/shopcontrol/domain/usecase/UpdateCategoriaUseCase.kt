package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CategoriaRepository

class UpdateCategoriaUseCase(
    private val repository: CategoriaRepository
) {

    suspend operator fun invoke(categoriaId: Long, nome: String): Boolean =
        repository.updateCategoria(categoriaId, nome)

}
