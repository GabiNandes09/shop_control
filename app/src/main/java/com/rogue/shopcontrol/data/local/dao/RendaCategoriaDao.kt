package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RendaCategoriaDao {


    @Insert
    suspend fun insert(
        categoria: RendaCategoriaEntity
    ): Long


    @Query("""
        SELECT * FROM renda_categorias
        ORDER BY nome ASC
    """)
    fun getAll(): Flow<List<RendaCategoriaEntity>>


    @Query("""
        SELECT * FROM renda_categorias
        WHERE nome = :nome COLLATE NOCASE
        LIMIT 1
    """)
    suspend fun findByName(
        nome: String
    ): RendaCategoriaEntity?


    @Query("""
        UPDATE renda_categorias
        SET nome = :nome
        WHERE id = :categoriaId
    """)
    suspend fun updateNome(
        categoriaId: Long,
        nome: String
    )


    @Query("""
        SELECT COUNT(*) FROM renda
        WHERE rendaCategoriaId = :categoriaId
    """)
    suspend fun countUsage(
        categoriaId: Long
    ): Int


    @Query("""
        DELETE FROM renda_categorias
        WHERE id = :categoriaId
    """)
    suspend fun delete(
        categoriaId: Long
    )

}
