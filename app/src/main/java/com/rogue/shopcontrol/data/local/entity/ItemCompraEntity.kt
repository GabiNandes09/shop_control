package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "itens_compra",
    foreignKeys = [
        ForeignKey(
            entity = CompraEntity::class,
            parentColumns = ["id"],
            childColumns = ["compraId"]
        ),
        ForeignKey(
            entity = ProdutoEntity::class,
            parentColumns = ["id"],
            childColumns = ["produtoId"]
        )
    ]
)
data class ItemCompraEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val compraId: Long,

    val produtoId: Long,

    val quantidade: Double,

    val valorUnitario: Double,

    val valorTotal: Double
)
