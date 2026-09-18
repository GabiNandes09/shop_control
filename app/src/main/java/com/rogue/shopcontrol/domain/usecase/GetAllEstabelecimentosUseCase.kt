package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import kotlinx.coroutines.flow.Flow

class GetAllEstabelecimentosUseCase(
    private val repository: EstabelecimentoRepository
) {

    operator fun invoke(): Flow<List<EstabelecimentoEntity>> =
        repository.getAll()

}
