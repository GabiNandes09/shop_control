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
        ItemCompraEntity::class,
        RendaEntity::class,
        RendaCategoriaEntity::class,
        FonteRendaEntity::class,
        ARecebimentoEntity::class,
        DivisaoContaEntity::class
    ],
    version = 10
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun estabelecimentoDao(): EstabelecimentoDao

    abstract fun produtoDao(): ProdutoDao

    abstract fun compraDao(): CompraDao

    abstract fun categoriaDao(): CategoriaDao

    abstract fun rendaDao(): RendaDao

    abstract fun rendaCategoriaDao(): RendaCategoriaDao

    abstract fun fonteRendaDao(): FonteRendaDao

    abstract fun arecebimentoDao(): ARecebimentoDao

    abstract fun divisaoContaDao(): DivisaoContaDao

}