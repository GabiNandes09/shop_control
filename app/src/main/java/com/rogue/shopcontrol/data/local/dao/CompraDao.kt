package com.rogue.shopcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {


    @Insert
    suspend fun insert(
        compra: CompraEntity
    ): Long


    @Insert
    suspend fun insertItens(
        itens: List<ItemCompraEntity>
    )


    @Transaction
    @Query("""
        SELECT * FROM compras
        ORDER BY dataCompra DESC
    """)
    fun getCompras(): Flow<List<CompraCompleta>>

}