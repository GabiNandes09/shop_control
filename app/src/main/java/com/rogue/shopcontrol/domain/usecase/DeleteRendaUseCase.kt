package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.RendaRepository

class DeleteRendaUseCase(
    private val repository: RendaRepository
) {

    suspend operator fun invoke(rendaId: Long) =
        repository.deleteRendaForward(rendaId)

}
