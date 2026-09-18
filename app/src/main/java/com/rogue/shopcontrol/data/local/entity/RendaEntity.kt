package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "renda",
    foreignKeys = [
        ForeignKey(
            entity = RendaCategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["rendaCategoriaId"]
        ),
        ForeignKey(
            entity = FonteRendaEntity::class,
            parentColumns = ["id"],
            childColumns = ["fonteRendaId"]
        )
    ]
)
data class RendaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val descricao: String,

    val valor: Double,

    val data: String,

    val rendaCategoriaId: Long,

    val fonteRendaId: Long,

    val recorrente: Boolean = false,

    val origemRecorrenteId: Long? = null
)
