package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.ProdutoDao
import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import kotlinx.coroutines.flow.Flow

class ProdutoRepository(
    private val produtoDao: ProdutoDao
) {

    fun getProdutoGastoById(
        produtoId: Long
    ): Flow<ProdutoGasto?> =
        produtoDao.getProdutoGastoById(produtoId)


    fun getHistoricoProduto(
        produtoId: Long
    ): Flow<List<ProdutoCompraHistorico>> =
        produtoDao.getHistoricoProduto(produtoId)


    fun getProdutoItensComData(): Flow<List<ProdutoItemComData>> =
        produtoDao.getProdutoItensComData()


    fun getAllProdutos(): Flow<List<ProdutoEntity>> =
        produtoDao.getAllProdutos()


    suspend fun findByName(
        nome: String
    ): ProdutoEntity? =
        produtoDao.findByName(nome.trim())


    suspend fun insert(
        produto: ProdutoEntity
    ): Long =
        produtoDao.insert(
            produto.copy(nome = capitalizarPrimeiraLetra(produto.nome))
        )


    suspend fun assignCategoria(
        produtoId: Long,
        categoriaId: Long
    ) =
        produtoDao.updateCategoria(produtoId, categoriaId)


    suspend fun updateCodigoBarras(
        produtoId: Long,
        codigoBarras: String?
    ) =
        produtoDao.updateCodigoBarras(produtoId, codigoBarras)


    suspend fun updateApelido(
        produtoId: Long,
        apelido: String?
    ) =
        produtoDao.updateApelido(
            produtoId,
            apelido?.let { capitalizarPrimeiraLetra(it) }
        )


    suspend fun mergeProdutos(
        origemId: Long,
        destinoId: Long
    ) =
        produtoDao.mergeProdutos(origemId, destinoId)


    suspend fun linkProdutosToCategoria(
        categoriaId: Long,
        produtoIdsParaVincular: List<Long>,
        produtoIdsParaDesvincular: List<Long>
    ) {

        if (produtoIdsParaVincular.isNotEmpty()) {
            produtoDao.updateCategoriaForIds(produtoIdsParaVincular, categoriaId)
        }

        if (produtoIdsParaDesvincular.isNotEmpty()) {
            produtoDao.updateCategoriaForIds(produtoIdsParaDesvincular, 0L)
        }

    }

}
