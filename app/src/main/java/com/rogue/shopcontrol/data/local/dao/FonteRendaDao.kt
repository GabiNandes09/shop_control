package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FonteRendaDao {


    @Insert
    suspend fun insert(
        fonte: FonteRendaEntity
    ): Long


    @Query("""
        SELECT * FROM fontes_renda
        ORDER BY nome ASC
    """)
    fun getAll(): Flow<List<FonteRendaEntity>>


    @Query("""
        SELECT * FROM fontes_renda
        WHERE nome = :nome COLLATE NOCASE
        LIMIT 1
    """)
    suspend fun findByNome(
        nome: String
    ): FonteRendaEntity?


    @Query("""
        SELECT * FROM fontes_renda
        WHERE id = :id
        LIMIT 1
    """)
    fun findById(id: Long): Flow<FonteRendaEntity?>

}
