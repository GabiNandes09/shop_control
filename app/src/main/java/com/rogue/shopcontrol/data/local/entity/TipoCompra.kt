package com.rogue.shopcontrol.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
enum class TipoCompra {
    VARIAVEL,
    FIXA,
    PARCELADA,
    RAPIDA
}
