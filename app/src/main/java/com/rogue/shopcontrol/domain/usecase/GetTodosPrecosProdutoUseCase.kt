package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ItemPrecoHistorico
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetTodosPrecosProdutoUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(): Flow<List<ItemPrecoHistorico>> =
        repository.getTodosPrecos()

}
