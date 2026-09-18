package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.FonteRendaRepository

class AddFonteRendaUseCase(
    private val repository: FonteRendaRepository
) {

    suspend operator fun invoke(nome: String): Long =
        repository.addFonte(nome)

}
