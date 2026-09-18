package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "divisao_conta",
    foreignKeys = [
        ForeignKey(
            entity = CompraEntity::class,
            parentColumns = ["id"],
            childColumns = ["compraId"]
        ),
        ForeignKey(
            entity = FonteRendaEntity::class,
            parentColumns = ["id"],
            childColumns = ["fonteRendaId"]
        )
    ]
)
data class DivisaoContaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val compraId: Long,

    val fonteRendaId: Long,

    val valor: Double

)
