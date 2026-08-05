package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomWarnings
import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {


    @Insert
    suspend fun insert(
        produto: ProdutoEntity
    ): Long


    @Query("""
        SELECT * FROM produtos
        WHERE nome = :nome
        LIMIT 1
    """)
    suspend fun findByName(
        nome: String
    ): ProdutoEntity?


    @SuppressWarnings(RoomWarnings.QUERY_MISMATCH)
    @Query("""
        SELECT p.id AS id, p.nome AS nome,
               SUM(i.valorTotal) AS valorTotalGasto,
               SUM(i.quantidade) AS quantidadeTotal
        FROM produtos p
        INNER JOIN itens_compra i ON i.produtoId = p.id
        GROUP BY p.id
        ORDER BY valorTotalGasto DESC
    """)
    fun getProdutosGasto(): Flow<List<ProdutoGasto>>


    @Query("""
        SELECT p.id AS id, p.nome AS nome,
               SUM(i.valorTotal) AS valorTotalGasto,
               SUM(i.quantidade) AS quantidadeTotal,
               c.id AS categoriaId,
               c.nome AS categoriaNome
        FROM produtos p
        INNER JOIN itens_compra i ON i.produtoId = p.id
        LEFT JOIN categorias c ON c.id = p.categoriaId
        WHERE p.id = :produtoId
        GROUP BY p.id
    """)
    fun getProdutoGastoById(produtoId: Long): Flow<ProdutoGasto?>


    @Query("""
        SELECT c.dataCompra AS dataCompra,
               e.nome AS nomeEstabelecimento,
               e.apelido AS apelidoEstabelecimento,
               i.quantidade AS quantidade,
               i.valorUnitario AS valorUnitario,
               i.valorTotal AS valorTotal
        FROM itens_compra i
        INNER JOIN compras c ON c.id = i.compraId
        INNER JOIN estabelecimentos e ON e.id = c.estabelecimentoId
        WHERE i.produtoId = :produtoId
        ORDER BY c.dataCompra DESC
    """)
    fun getHistoricoProduto(produtoId: Long): Flow<List<ProdutoCompraHistorico>>


    @Query("""
        SELECT p.id AS produtoId, p.nome AS nomeProduto,
               c.dataCompra AS dataCompra,
               i.quantidade AS quantidade,
               i.valorTotal AS valorTotal
        FROM itens_compra i
        INNER JOIN produtos p ON p.id = i.produtoId
        INNER JOIN compras c ON c.id = i.compraId
    """)
    fun getProdutoItensComData(): Flow<List<ProdutoItemComData>>


    @Query("""
        SELECT * FROM produtos
        ORDER BY nome ASC
    """)
    fun getAllProdutos(): Flow<List<ProdutoEntity>>


    @Query("""
        UPDATE produtos
        SET categoriaId = :categoriaId
        WHERE id = :produtoId
    """)
    suspend fun updateCategoria(
        produtoId: Long,
        categoriaId: Long
    )

}