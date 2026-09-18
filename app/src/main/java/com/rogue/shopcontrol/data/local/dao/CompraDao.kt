package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rogue.shopcontrol.data.local.entity.CompraComData
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
import com.rogue.shopcontrol.data.local.entity.ItemPrecoHistorico
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {


    @Insert
    suspend fun insert(
        compra: CompraEntity
    ): Long


    @Insert
    suspend fun insertItens(
        itens: List<ItemCompraEntity>
    )


    @Transaction
    @Query("""
        SELECT * FROM compras
        ORDER BY substr(dataCompra,7,4) || substr(dataCompra,4,2) || substr(dataCompra,1,2) || substr(dataCompra,12,8) DESC, id DESC
    """)
    fun getCompras(): Flow<List<CompraCompleta>>


    @Transaction
    @Query("""
        SELECT * FROM compras
        WHERE id = :id
    """)
    fun getCompraById(id: Long): Flow<CompraCompleta?>


    @Transaction
    @Query("""
        SELECT * FROM compras
        WHERE estabelecimentoId = :estabelecimentoId
        ORDER BY substr(dataCompra,7,4) || substr(dataCompra,4,2) || substr(dataCompra,1,2) || substr(dataCompra,12,8) DESC, id DESC
    """)
    fun getComprasByEstabelecimento(estabelecimentoId: Long): Flow<List<CompraCompleta>>


    @Query("""
        SELECT c.id FROM compras c
        INNER JOIN estabelecimentos e ON e.id = c.estabelecimentoId
        WHERE e.cnpj = :cnpjEstabelecimento
        AND (c.dataCompra = :dataCompra OR (c.dataCompra IS NULL AND :dataCompra IS NULL))
        LIMIT 1
    """)
    suspend fun findCompraId(
        cnpjEstabelecimento: String,
        dataCompra: String?
    ): Long?


    @Query("""
        DELETE FROM itens_compra
        WHERE compraId = :compraId
    """)
    suspend fun deleteItensByCompraId(compraId: Long)


    @Query("""
        SELECT c.id AS compraId, i.produtoId AS produtoId, c.dataCompra AS dataCompra, i.valorUnitario AS valorUnitario
        FROM itens_compra i
        INNER JOIN compras c ON c.id = i.compraId
    """)
    fun getTodosPrecos(): Flow<List<ItemPrecoHistorico>>


    @Query("""
        UPDATE compras
        SET dataCompra = :dataCompra, estabelecimentoId = :estabelecimentoId, valorTotal = :valorTotal, categoriaId = :categoriaId
        WHERE id = :compraId
    """)
    suspend fun updateManualInfo(
        compraId: Long,
        dataCompra: String?,
        estabelecimentoId: Long,
        valorTotal: Double,
        categoriaId: Long?
    )


    @Query("""
        UPDATE compras
        SET categoriaId = :categoriaId
        WHERE estabelecimentoId = :estabelecimentoId
          AND tipo = 'VARIAVEL'
          AND categoriaId IS NULL
    """)
    suspend fun backfillCategoriaForEstabelecimento(
        estabelecimentoId: Long,
        categoriaId: Long
    )


    @Query("""
        DELETE FROM compras
        WHERE id = :compraId
    """)
    suspend fun deleteCompra(compraId: Long)


    @Transaction
    suspend fun deleteCompraCompleta(compraId: Long) {
        deleteItensByCompraId(compraId)
        deleteCompra(compraId)
    }


    @Query("""
        SELECT * FROM compras
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findByIdOnce(id: Long): CompraEntity?


    @Query("""
        SELECT * FROM compras
        WHERE origemRecorrenteId = :origemId
        LIMIT 1
    """)
    suspend fun findByOrigemRecorrenteId(origemId: Long): CompraEntity?


    @Query("""
        UPDATE compras
        SET nome = :nome, valorTotal = :valorTotal, categoriaId = :categoriaId, estabelecimentoId = :estabelecimentoId
        WHERE id = :id
    """)
    suspend fun updateCamposFixaParcelada(
        id: Long,
        nome: String,
        valorTotal: Double,
        categoriaId: Long,
        estabelecimentoId: Long
    )


    @Query("""
        UPDATE compras
        SET ativo = :ativo
        WHERE id = :id
    """)
    suspend fun setAtivo(id: Long, ativo: Boolean)


    @Query("""
        DELETE FROM compras
        WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Long>)


    @Query("""
        SELECT * FROM compras
        WHERE tipo = 'FIXA'
        AND ativo = 1
        AND id NOT IN (
            SELECT origemRecorrenteId FROM compras
            WHERE origemRecorrenteId IS NOT NULL
        )
    """)
    suspend fun findFixaTailsAtivas(): List<CompraEntity>


    @Query("""
        SELECT c.id AS compraId, c.estabelecimentoId AS estabelecimentoId, c.dataCompra AS dataCompra,
               c.dataCompetencia AS dataCompetencia, c.valorTotal AS valorTotal, c.tipo AS tipo,
               c.categoriaId AS categoriaId, cat.nome AS categoriaNome
        FROM compras c
        LEFT JOIN categorias cat ON cat.id = c.categoriaId
    """)
    fun getComprasComData(): Flow<List<CompraComData>>

}