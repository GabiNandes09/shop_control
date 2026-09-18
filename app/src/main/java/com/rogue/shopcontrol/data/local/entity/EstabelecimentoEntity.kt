package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "estabelecimentos"
)
data class EstabelecimentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val cnpj: String,

    val endereco: String,

    val apelido: String? = null,

    val categoriaId: Long? = null

)
