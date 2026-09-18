package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {


    @Insert
    suspend fun insert(
        categoria: CategoriaEntity
    ): Long


    @Query("""
        SELECT * FROM categorias
        ORDER BY nome ASC
    """)
    fun getAll(): Flow<List<CategoriaEntity>>


    @Query("""
        SELECT * FROM categorias
        WHERE nome = :nome COLLATE NOCASE
        LIMIT 1
    """)
    suspend fun findByName(
        nome: String
    ): CategoriaEntity?


    @Query("""
        SELECT * FROM categorias
        WHERE id = :categoriaId
        LIMIT 1
    """)
    suspend fun findById(
        categoriaId: Long
    ): CategoriaEntity?


    @Query("""
        DELETE FROM categorias
        WHERE id = :categoriaId
    """)
    suspend fun delete(
        categoriaId: Long
    )


    @Query("""
        UPDATE categorias
        SET nome = :nome
        WHERE id = :categoriaId
    """)
    suspend fun updateNome(
        categoriaId: Long,
        nome: String
    )


    @Query("""
        UPDATE categorias
        SET grupoId = :grupoId
        WHERE id = :categoriaId
    """)
    suspend fun updateGrupo(
        categoriaId: Long,
        grupoId: Long?
    )


    @Query("""
        SELECT
            (SELECT COUNT(*) FROM produtos WHERE categoriaId = :categoriaId) +
            (SELECT COUNT(*) FROM compras WHERE categoriaId = :categoriaId) +
            (SELECT COUNT(*) FROM estabelecimentos WHERE categoriaId = :categoriaId)
    """)
    suspend fun countUsage(
        categoriaId: Long
    ): Int

}
