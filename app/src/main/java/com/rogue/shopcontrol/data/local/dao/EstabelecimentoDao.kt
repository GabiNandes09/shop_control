package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraComData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstabelecimentoDao {

    @Insert
    suspend fun insert(
        estabelecimento: EstabelecimentoEntity
    ): Long


    @Query("SELECT * FROM estabelecimentos")
    fun getAll(): Flow<List<EstabelecimentoEntity>>


    @Query("""
        SELECT * FROM estabelecimentos
        WHERE cnpj = :cnpj
        LIMIT 1
    """)
    suspend fun findByCnpj(
        cnpj: String
    ): EstabelecimentoEntity?


    @Query("""
        SELECT * FROM estabelecimentos
        WHERE nome = :nome COLLATE NOCASE
        LIMIT 1
    """)
    suspend fun findByNome(
        nome: String
    ): EstabelecimentoEntity?


    @Query("""
        SELECT * FROM estabelecimentos
        WHERE id = :id
        LIMIT 1
    """)
    fun findById(id: Long): Flow<EstabelecimentoEntity?>


    @Query("""
        SELECT e.id AS estabelecimentoId, e.nome AS nome, e.apelido AS apelido,
               c.dataCompra AS dataCompra, c.dataCompetencia AS dataCompetencia,
               c.valorTotal AS valorTotal, c.tipo AS tipo
        FROM compras c
        INNER JOIN estabelecimentos e ON e.id = c.estabelecimentoId
    """)
    fun getComprasComEstabelecimento(): Flow<List<EstabelecimentoCompraComData>>


    @Query("""
        SELECT dataCompra, dataCompetencia, valorTotal, tipo FROM compras
        WHERE estabelecimentoId = :estabelecimentoId
    """)
    fun getComprasPorEstabelecimento(estabelecimentoId: Long): Flow<List<EstabelecimentoCompraData>>


    @Query("""
        UPDATE estabelecimentos
        SET apelido = :apelido
        WHERE id = :estabelecimentoId
    """)
    suspend fun updateApelido(
        estabelecimentoId: Long,
        apelido: String?
    )


    @Query("""
        UPDATE estabelecimentos
        SET categoriaId = :categoriaId
        WHERE id = :estabelecimentoId
    """)
    suspend fun updateCategoria(
        estabelecimentoId: Long,
        categoriaId: Long?
    )


    @Query("""
        UPDATE compras
        SET estabelecimentoId = :novoEstabelecimentoId
        WHERE estabelecimentoId = :antigoEstabelecimentoId
    """)
    suspend fun reassignCompras(
        antigoEstabelecimentoId: Long,
        novoEstabelecimentoId: Long
    )


    @Query("""
        DELETE FROM estabelecimentos
        WHERE id = :estabelecimentoId
    """)
    suspend fun delete(
        estabelecimentoId: Long
    )


    @Transaction
    suspend fun linkToEstabelecimentoComCnpj(
        semCnpjId: Long,
        comCnpjId: Long
    ) {
        reassignCompras(semCnpjId, comCnpjId)
        delete(semCnpjId)
    }

}
