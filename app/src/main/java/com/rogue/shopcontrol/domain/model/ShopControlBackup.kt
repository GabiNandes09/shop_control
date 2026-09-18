package com.rogue.shopcontrol.domain.model

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.data.local.entity.RendaEntity
import kotlinx.serialization.Serializable

const val BACKUP_SCHEMA_VERSION = 6

@Serializable
data class ShopControlBackup(
    val schemaVersion: Int,
    val exportedAt: String,
    val estabelecimentos: List<EstabelecimentoEntity>,
    val produtos: List<ProdutoEntity>,
    val categorias: List<CategoriaEntity>,
    val compras: List<CompraEntity>,
    val itensCompra: List<ItemCompraEntity>,
    val rendaCategorias: List<RendaCategoriaEntity> = emptyList(),
    val fontesRenda: List<FonteRendaEntity> = emptyList(),
    val rendas: List<RendaEntity> = emptyList()
)
