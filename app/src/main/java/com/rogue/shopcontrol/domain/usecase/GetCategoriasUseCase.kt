package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriasUseCase(
    private val repository: CategoriaRepository
) {

    operator fun invoke(): Flow<List<CategoriaEntity>> =
        repository.getCategorias()

}
