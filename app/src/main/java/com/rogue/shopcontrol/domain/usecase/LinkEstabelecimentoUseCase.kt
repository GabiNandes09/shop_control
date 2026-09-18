package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository

class LinkEstabelecimentoUseCase(
    private val repository: EstabelecimentoRepository
) {

    suspend operator fun invoke(semCnpjId: Long, comCnpjId: Long) =
        repository.linkToEstabelecimentoComCnpj(semCnpjId, comCnpjId)

}
