package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
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
        ORDER BY dataCompra DESC
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
        ORDER BY dataCompra DESC
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
        DELETE FROM compras
        WHERE id = :compraId
    """)
    suspend fun deleteCompra(compraId: Long)


    @Transaction
    suspend fun deleteCompraCompleta(compraId: Long) {
        deleteItensByCompraId(compraId)
        deleteCompra(compraId)
    }

}