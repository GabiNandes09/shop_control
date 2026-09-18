package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.DivisaoParticipanteComNome
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetDivisaoContaUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(compraId: Long): Flow<List<DivisaoParticipanteComNome>> =
        repository.getDivisaoConta(compraId)

}
