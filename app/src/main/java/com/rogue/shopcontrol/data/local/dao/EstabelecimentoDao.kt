package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
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
        WHERE id = :id
        LIMIT 1
    """)
    fun findById(id: Long): Flow<EstabelecimentoEntity?>


    @Query("""
        SELECT e.id AS id, e.nome AS nome, e.apelido AS apelido,
               SUM(c.valorTotal) AS valorTotalGasto
        FROM estabelecimentos e
        INNER JOIN compras c ON c.estabelecimentoId = e.id
        GROUP BY e.id
        ORDER BY valorTotalGasto DESC
    """)
    fun getEstabelecimentosGasto(): Flow<List<EstabelecimentoGasto>>


    @Query("""
        SELECT dataCompra, valorTotal FROM compras
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

}
