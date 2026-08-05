package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CategoriaRepository

class AddCategoriaUseCase(
    private val repository: CategoriaRepository
) {

    suspend operator fun invoke(nome: String): Boolean =
        repository.addCategoria(nome)

}
