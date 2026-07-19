package com.rogue.shopcontrol.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ItemCompraCompleto(

    @Embedded
    val item: ItemCompraEntity,


    @Relation(
        parentColumn = "produtoId",
        entityColumn = "id"
    )
    val produto: ProdutoEntity
)
