package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.ProdutoDao
import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import kotlinx.coroutines.flow.Flow

class ProdutoRepository(
    private val produtoDao: ProdutoDao
) {

    fun getProdutosGasto(): Flow<List<ProdutoGasto>> =
        produtoDao.getProdutosGasto()


    fun getProdutoGastoById(
        produtoId: Long
    ): Flow<ProdutoGasto?> =
        produtoDao.getProdutoGastoById(produtoId)


    fun getHistoricoProduto(
        produtoId: Long
    ): Flow<List<ProdutoCompraHistorico>> =
        produtoDao.getHistoricoProduto(produtoId)

}
