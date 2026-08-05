package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import kotlinx.coroutines.flow.Flow

class GetEstabelecimentosGastoUseCase(
    private val repository: EstabelecimentoRepository
) {

    operator fun invoke(): Flow<List<EstabelecimentoGasto>> =
        repository.getEstabelecimentosGasto()

}
