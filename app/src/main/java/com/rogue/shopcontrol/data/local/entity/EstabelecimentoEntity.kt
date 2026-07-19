package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "estabelecimentos"
)
data class EstabelecimentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val cnpj: String,

    val endereco: String

)
