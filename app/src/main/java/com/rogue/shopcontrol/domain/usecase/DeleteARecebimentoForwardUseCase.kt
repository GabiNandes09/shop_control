package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ARecebimentoRepository

class DeleteARecebimentoForwardUseCase(
    private val repository: ARecebimentoRepository
) {

    suspend operator fun invoke(arecebimentoId: Long) =
        repository.deleteARecebimentoForward(arecebimentoId)

}
