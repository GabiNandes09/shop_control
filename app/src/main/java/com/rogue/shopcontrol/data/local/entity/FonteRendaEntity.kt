package com.rogue.shopcontrol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "fontes_renda"
)
data class FonteRendaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String
)
