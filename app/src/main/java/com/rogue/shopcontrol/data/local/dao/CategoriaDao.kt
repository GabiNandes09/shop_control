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
        DELETE FROM categorias
        WHERE id = :categoriaId
    """)
    suspend fun delete(
        categoriaId: Long
    )

}
