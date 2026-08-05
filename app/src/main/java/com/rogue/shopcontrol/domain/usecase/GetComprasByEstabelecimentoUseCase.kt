package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.repository.CompraRepository
import kotlinx.coroutines.flow.Flow

class GetComprasByEstabelecimentoUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(estabelecimentoId: Long): Flow<List<CompraCompleta>> =
        repository.getComprasByEstabelecimento(estabelecimentoId)

}
