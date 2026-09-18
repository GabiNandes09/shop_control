package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CategoriaRepository

class AddCategoriaERetornarIdUseCase(
    private val repository: CategoriaRepository
) {

    suspend operator fun invoke(nome: String): Long =
        repository.addCategoriaERetornarId(nome)

}
