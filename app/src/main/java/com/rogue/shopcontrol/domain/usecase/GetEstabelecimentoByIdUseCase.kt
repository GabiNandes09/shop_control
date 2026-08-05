package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import kotlinx.coroutines.flow.Flow

class GetEstabelecimentoByIdUseCase(
    private val repository: EstabelecimentoRepository
) {

    operator fun invoke(estabelecimentoId: Long): Flow<EstabelecimentoEntity?> =
        repository.findById(estabelecimentoId)

}
