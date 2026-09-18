package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.data.repository.RendaRepository
import kotlinx.coroutines.flow.Flow

class GetRendaComDadosUseCase(
    private val repository: RendaRepository
) {

    operator fun invoke(): Flow<List<RendaComDados>> =
        repository.getRendaComDados()

}
