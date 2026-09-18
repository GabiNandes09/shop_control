package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.DivisaoContaEntity
import com.rogue.shopcontrol.data.local.entity.DivisaoParticipanteComNome
import kotlinx.coroutines.flow.Flow

@Dao
interface DivisaoContaDao {


    @Insert
    suspend fun insert(
        divisao: DivisaoContaEntity
    ): Long


    @Query("""
        SELECT d.fonteRendaId AS fonteRendaId, f.nome AS fonteNome, d.valor AS valor
        FROM divisao_conta d
        INNER JOIN fontes_renda f ON f.id = d.fonteRendaId
        WHERE d.compraId = :compraId
    """)
    fun getByCompraId(compraId: Long): Flow<List<DivisaoParticipanteComNome>>


    @Query("""
        SELECT d.fonteRendaId AS fonteRendaId, f.nome AS fonteNome, d.valor AS valor
        FROM divisao_conta d
        INNER JOIN fontes_renda f ON f.id = d.fonteRendaId
        WHERE d.compraId = :compraId
    """)
    suspend fun getByCompraIdOnce(compraId: Long): List<DivisaoParticipanteComNome>


    @Query("""
        DELETE FROM divisao_conta
        WHERE compraId IN (:compraIds)
    """)
    suspend fun deleteByCompraIds(compraIds: List<Long>)

}
