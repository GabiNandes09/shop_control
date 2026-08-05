package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository

class UpdateApelidoEstabelecimentoUseCase(
    private val repository: EstabelecimentoRepository
) {

    suspend operator fun invoke(estabelecimentoId: Long, apelido: String?) =
        repository.updateApelido(estabelecimentoId, apelido)

}
