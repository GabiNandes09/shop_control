package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow

class GetProdutoItensComDataUseCase(
    private val repository: ProdutoRepository
) {

    operator fun invoke(): Flow<List<ProdutoItemComData>> =
        repository.getProdutoItensComData()

}
