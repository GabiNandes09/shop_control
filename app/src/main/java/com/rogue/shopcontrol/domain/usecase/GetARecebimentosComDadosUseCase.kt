package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.repository.ARecebimentoRepository
import kotlinx.coroutines.flow.Flow

class GetARecebimentosComDadosUseCase(
    private val repository: ARecebimentoRepository
) {

    operator fun invoke(): Flow<List<ARecebimentoComDados>> =
        repository.getARecebimentosComDados()

}
