package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.data.local.entity.RendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RendaDao {


    @Insert
    suspend fun insert(
        renda: RendaEntity
    ): Long


    @Query("""
        SELECT r.id AS id, r.descricao AS descricao, r.valor AS valor, r.data AS data,
               r.recorrente AS recorrente, r.origemRecorrenteId AS origemRecorrenteId,
               r.rendaCategoriaId AS rendaCategoriaId, rc.nome AS categoriaNome,
               r.fonteRendaId AS fonteRendaId, fr.nome AS fonteNome
        FROM renda r
        INNER JOIN renda_categorias rc ON rc.id = r.rendaCategoriaId
        INNER JOIN fontes_renda fr ON fr.id = r.fonteRendaId
        ORDER BY substr(r.data,7,4) || substr(r.data,4,2) || substr(r.data,1,2) || substr(r.data,12,8) DESC, r.id DESC
    """)
    fun getRendaComDados(): Flow<List<RendaComDados>>


    @Query("""
        SELECT * FROM renda
        ORDER BY id ASC
    """)
    fun getAllRaw(): Flow<List<RendaEntity>>


    @Query("""
        SELECT * FROM renda
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findByIdOnce(id: Long): RendaEntity?


    @Query("""
        SELECT * FROM renda
        WHERE origemRecorrenteId = :origemId
        LIMIT 1
    """)
    suspend fun findByOrigemRecorrenteId(origemId: Long): RendaEntity?


    @Query("""
        UPDATE renda
        SET descricao = :descricao, valor = :valor, rendaCategoriaId = :categoriaId, fonteRendaId = :fonteId
        WHERE id = :id
    """)
    suspend fun updateCampos(
        id: Long,
        descricao: String,
        valor: Double,
        categoriaId: Long,
        fonteId: Long
    )


    @Query("""
        UPDATE renda
        SET recorrente = :recorrente
        WHERE id = :id
    """)
    suspend fun setRecorrente(
        id: Long,
        recorrente: Boolean
    )


    @Query("""
        DELETE FROM renda
        WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Long>)


    @Query("""
        SELECT * FROM renda
        WHERE recorrente = 1
        AND id NOT IN (
            SELECT origemRecorrenteId FROM renda
            WHERE origemRecorrenteId IS NOT NULL
        )
    """)
    suspend fun findTailsAtivas(): List<RendaEntity>

}
