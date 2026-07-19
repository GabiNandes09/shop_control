package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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
}