package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.data.repository.RendaCategoriaRepository
import kotlinx.coroutines.flow.Flow

class GetRendaCategoriasUseCase(
    private val repository: RendaCategoriaRepository
) {

    operator fun invoke(): Flow<List<RendaCategoriaEntity>> =
        repository.getCategorias()

}
