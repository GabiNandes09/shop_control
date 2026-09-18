package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
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

    val chaveNfce: String? = null,

    val origem: OrigemCompra = OrigemCompra.NFC_E,

    val tipo: TipoCompra = TipoCompra.VARIAVEL,

    val nome: String? = null,

    val categoriaId: Long? = null,

    val origemRecorrenteId: Long? = null,

    val totalParcelas: Int? = null,

    val dataCompetencia: String? = null,

    val ativo: Boolean = true,

    val dividida: Boolean = false
) {

    val dataParaFiltro: String?
        get() = if (tipo == TipoCompra.PARCELADA) dataCompetencia else dataCompra

}
