package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraComData
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import kotlinx.coroutines.flow.Flow

class GetTodasComprasComEstabelecimentoUseCase(
    private val repository: EstabelecimentoRepository
) {

    operator fun invoke(): Flow<List<EstabelecimentoCompraComData>> =
        repository.getComprasComEstabelecimento()

}
