package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CategoriaGasto
import com.rogue.shopcontrol.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriasGastoUseCase(
    private val repository: CategoriaRepository
) {

    operator fun invoke(): Flow<List<CategoriaGasto>> =
        repository.getCategoriasGasto()

}
