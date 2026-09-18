package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.local.entity.ARecebimentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ARecebimentoDao {


    @Insert
    suspend fun insert(
        arecebimento: ARecebimentoEntity
    ): Long


    @Query("""
        SELECT a.id AS id, a.descricao AS descricao, a.valor AS valor, a.dataPrevista AS dataPrevista,
               a.tipo AS tipo, a.totalParcelas AS totalParcelas, a.origemRecorrenteId AS origemRecorrenteId,
               a.pago AS pago, a.dataPagamento AS dataPagamento, a.rendaGeradaId AS rendaGeradaId,
               a.rendaCategoriaId AS rendaCategoriaId, rc.nome AS categoriaNome,
               a.fonteRendaId AS fonteRendaId, fr.nome AS fonteNome
        FROM arecebimentos a
        INNER JOIN renda_categorias rc ON rc.id = a.rendaCategoriaId
        INNER JOIN fontes_renda fr ON fr.id = a.fonteRendaId
        ORDER BY substr(a.dataPrevista,7,4) || substr(a.dataPrevista,4,2) || substr(a.dataPrevista,1,2) || substr(a.dataPrevista,12,8) DESC, a.id DESC
    """)
    fun getARecebimentosComDados(): Flow<List<ARecebimentoComDados>>


    @Query("""
        SELECT * FROM arecebimentos
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findByIdOnce(id: Long): ARecebimentoEntity?


    @Query("""
        SELECT * FROM arecebimentos
        WHERE origemRecorrenteId = :origemId
        LIMIT 1
    """)
    suspend fun findByOrigemRecorrenteId(origemId: Long): ARecebimentoEntity?


    @Query("""
        UPDATE arecebimentos
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
        UPDATE arecebimentos
        SET ativo = :ativo
        WHERE id = :id
    """)
    suspend fun setAtivo(
        id: Long,
        ativo: Boolean
    )


    @Query("""
        UPDATE arecebimentos
        SET pago = 1, dataPagamento = :dataPagamento, rendaGeradaId = :rendaGeradaId
        WHERE id = :id
    """)
    suspend fun marcarComoPago(
        id: Long,
        dataPagamento: String,
        rendaGeradaId: Long
    )


    @Query("""
        DELETE FROM arecebimentos
        WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Long>)


    @Query("""
        SELECT * FROM arecebimentos
        WHERE compraOrigemId IN (:compraIds)
    """)
    suspend fun findByCompraOrigemIds(compraIds: List<Long>): List<ARecebimentoEntity>


    @Query("""
        SELECT * FROM arecebimentos
        WHERE tipo = 'FIXA' AND ativo = 1
        AND id NOT IN (
            SELECT origemRecorrenteId FROM arecebimentos
            WHERE origemRecorrenteId IS NOT NULL
        )
    """)
    suspend fun findFixaTailsAtivas(): List<ARecebimentoEntity>

}
