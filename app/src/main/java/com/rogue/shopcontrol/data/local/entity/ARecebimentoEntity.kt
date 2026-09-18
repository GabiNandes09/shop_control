package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "arecebimentos",
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
data class ARecebimentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val descricao: String,

    val valor: Double,

    val dataPrevista: String,

    val rendaCategoriaId: Long,

    val fonteRendaId: Long,

    val tipo: TipoARecebimento = TipoARecebimento.PONTUAL,

    val totalParcelas: Int? = null,

    val origemRecorrenteId: Long? = null,

    val ativo: Boolean = true,

    val pago: Boolean = false,

    val dataPagamento: String? = null,

    val rendaGeradaId: Long? = null,

    val compraOrigemId: Long? = null
)
