package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CategoriaRepository

class UpdateGrupoCategoriaUseCase(
    private val repository: CategoriaRepository
) {

    suspend operator fun invoke(categoriaId: Long, grupoId: Long?) =
        repository.updateGrupo(categoriaId, grupoId)

}
