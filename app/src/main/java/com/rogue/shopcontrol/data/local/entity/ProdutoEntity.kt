package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "produtos"
)
data class ProdutoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val codigo: String? = null,

    val categoriaId: Long = 0,

    val codigoBarras: String? = null,

    val apelido: String? = null
)
