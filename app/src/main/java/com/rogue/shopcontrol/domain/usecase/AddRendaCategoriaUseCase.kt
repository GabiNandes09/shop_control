package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaCategoriaRepository

class AddRendaCategoriaUseCase(
    private val repository: RendaCategoriaRepository
) {

    suspend operator fun invoke(nome: String): Boolean =
        repository.addCategoria(nome)

}
