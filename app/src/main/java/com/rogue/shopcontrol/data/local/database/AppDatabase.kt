package com.rogue.shopcontrol.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rogue.shopcontrol.data.local.dao.*
import com.rogue.shopcontrol.data.local.entity.*

@Database(
    entities = [
        EstabelecimentoEntity::class,
        ProdutoEntity::class,
        CategoriaEntity::class,
        CompraEntity::class,
        ItemCompraEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun estabelecimentoDao(): EstabelecimentoDao

    abstract fun produtoDao(): ProdutoDao

    abstract fun compraDao(): CompraDao

    abstract fun categoriaDao(): CategoriaDao

}