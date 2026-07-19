package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity

@Dao
interface ProdutoDao {


    @Insert
    suspend fun insert(
        produto: ProdutoEntity
    ): Long


    @Query("""
        SELECT * FROM produtos 
        WHERE nome = :nome
        LIMIT 1
    """)
    suspend fun findByName(
        nome: String
    ): ProdutoEntity?

}