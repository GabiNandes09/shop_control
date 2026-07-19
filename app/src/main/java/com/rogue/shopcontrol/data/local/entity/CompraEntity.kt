package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "compras",
    foreignKeys = [
        ForeignKey(
            entity = EstabelecimentoEntity::class,
            parentColumns = ["id"],
            childColumns = ["estabelecimentoId"]
        )
    ]
)
data class CompraEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val estabelecimentoId: Long,

    val dataCompra: String?,

    val valorTotal: Double,

    val chaveNfce: String? = null
)
