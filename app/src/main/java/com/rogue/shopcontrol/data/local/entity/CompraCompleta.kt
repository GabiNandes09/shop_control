package com.rogue.shopcontrol.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompraCompleta(

    @Embedded
    val compra: CompraEntity,

    @Relation(
        parentColumn = "estabelecimentoId",
        entityColumn = "id"
    )
    val estabelecimento: EstabelecimentoEntity,


    @Relation(
        entity = ItemCompraEntity::class,
        parentColumn = "id",
        entityColumn = "compraId"
    )
    val itens: List<ItemCompraCompleto>
)
