package com.rogue.shopcontrol.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.room.withTransaction
import com.rogue.shopcontrol.data.local.dao.CategoriaDao
import com.rogue.shopcontrol.data.local.dao.CompraDao
import com.rogue.shopcontrol.data.local.dao.EstabelecimentoDao
import com.rogue.shopcontrol.data.local.dao.FonteRendaDao
import com.rogue.shopcontrol.data.local.dao.ProdutoDao
import com.rogue.shopcontrol.data.local.dao.RendaCategoriaDao
import com.rogue.shopcontrol.data.local.dao.RendaDao
import com.rogue.shopcontrol.data.local.database.AppDatabase
import com.rogue.shopcontrol.domain.model.BACKUP_SCHEMA_VERSION
import com.rogue.shopcontrol.domain.model.ImportResult
import com.rogue.shopcontrol.domain.model.ShopControlBackup
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

private val BACKUP_JSON = Json { prettyPrint = true; ignoreUnknownKeys = true }

class BackupRepository(
    private val context: Context,
    private val estabelecimentoRepository: EstabelecimentoRepository,
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val compraRepository: CompraRepository,
    private val rendaRepository: RendaRepository,
    private val rendaCategoriaRepository: RendaCategoriaRepository,
    private val fonteRendaRepository: FonteRendaRepository,
    private val database: AppDatabase,
    private val estabelecimentoDao: EstabelecimentoDao,
    private val categoriaDao: CategoriaDao,
    private val produtoDao: ProdutoDao,
    private val compraDao: CompraDao,
    private val rendaCategoriaDao: RendaCategoriaDao,
    private val fonteRendaDao: FonteRendaDao,
    private val rendaDao: RendaDao
) {

    suspend fun exportBackup(): Uri {

        val estabelecimentos = estabelecimentoRepository.getAll().first()
        val produtos = produtoRepository.getAllProdutos().first()
        val categorias = categoriaRepository.getCategorias().first()
        val compras = compraRepository.getCompras().first()
        val rendaCategorias = rendaCategoriaRepository.getCategorias().first()
        val fontesRenda = fonteRendaRepository.getAll().first()
        val rendas = rendaRepository.getAllRendas().first()

        val backup =
            ShopControlBackup(
                schemaVersion = BACKUP_SCHEMA_VERSION,
                exportedAt = LocalDateTime.now().toString(),
                estabelecimentos = estabelecimentos,
                produtos = produtos,
                categorias = categorias,
                compras = compras.map { it.compra },
                itensCompra = compras.flatMap { compraCompleta ->
                    compraCompleta.itens.map { it.item }
                },
                rendaCategorias = rendaCategorias,
                fontesRenda = fontesRenda,
                rendas = rendas
            )

        val json =
            BACKUP_JSON.encodeToString(ShopControlBackup.serializer(), backup)

        val fileName = "shopcontrol_backup_${LocalDate.now()}.json"

        val backupDir =
            File(context.cacheDir, "backups").apply { mkdirs() }

        val file = File(backupDir, fileName)

        file.writeText(json)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

    }


    suspend fun importBackup(
        uri: Uri
    ): ImportResult {

        val texto =
            context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.readBytes().decodeToString()
            } ?: throw IllegalArgumentException("Não foi possível ler o arquivo")

        val backup =
            BACKUP_JSON.decodeFromString(ShopControlBackup.serializer(), texto)

        return database.withTransaction {

            val estabelecimentoIdMap = mutableMapOf<Long, Long>()
            val categoriaIdMap = mutableMapOf<Long, Long>()
            val produtoIdMap = mutableMapOf<Long, Long>()
            val rendaCategoriaIdMap = mutableMapOf<Long, Long>()
            val fonteRendaIdMap = mutableMapOf<Long, Long>()
            val compraIdMap = mutableMapOf<Long, Long>()
            val rendaIdMap = mutableMapOf<Long, Long>()

            backup.categorias.forEach { categoria ->

                val novoId =
                    categoriaDao.findByName(categoria.nome)?.id
                        ?: categoriaDao.insert(categoria.copy(id = 0))

                categoriaIdMap[categoria.id] = novoId

            }

            backup.estabelecimentos.forEach { estabelecimento ->

                val existente =
                    if (estabelecimento.cnpj.isNotBlank()) {
                        estabelecimentoDao.findByCnpj(estabelecimento.cnpj)
                    } else {
                        estabelecimentoDao.findByNome(estabelecimento.nome)
                    }

                val novaCategoriaId =
                    estabelecimento.categoriaId?.let { categoriaIdMap[it] }

                val novoId =
                    existente?.id
                        ?: estabelecimentoDao.insert(
                            estabelecimento.copy(id = 0, categoriaId = novaCategoriaId)
                        )

                estabelecimentoIdMap[estabelecimento.id] = novoId

            }

            backup.produtos.forEach { produto ->

                val existente =
                    produtoDao.findByName(produto.nome)

                val categoriaRemapeada =
                    if (produto.categoriaId == 0L) {
                        0L
                    } else {
                        categoriaIdMap[produto.categoriaId] ?: 0L
                    }

                val novoId =
                    existente?.id
                        ?: produtoDao.insert(
                            produto.copy(id = 0, categoriaId = categoriaRemapeada)
                        )

                produtoIdMap[produto.id] = novoId

            }

            backup.rendaCategorias.forEach { categoria ->

                val novoId =
                    rendaCategoriaDao.findByName(categoria.nome)?.id
                        ?: rendaCategoriaDao.insert(categoria.copy(id = 0))

                rendaCategoriaIdMap[categoria.id] = novoId

            }

            backup.fontesRenda.forEach { fonte ->

                val novoId =
                    fonteRendaDao.findByNome(fonte.nome)?.id
                        ?: fonteRendaDao.insert(fonte.copy(id = 0))

                fonteRendaIdMap[fonte.id] = novoId

            }

            var comprasImportadas = 0

            backup.compras.forEach { compra ->

                val novoEstabelecimentoId =
                    estabelecimentoIdMap[compra.estabelecimentoId] ?: return@forEach

                val novaCategoriaId =
                    compra.categoriaId?.let { categoriaIdMap[it] }

                val novoOrigemId =
                    compra.origemRecorrenteId?.let { compraIdMap[it] }

                val novoId =
                    compraDao.insert(
                        compra.copy(
                            id = 0,
                            estabelecimentoId = novoEstabelecimentoId,
                            categoriaId = novaCategoriaId,
                            origemRecorrenteId = novoOrigemId
                        )
                    )

                compraIdMap[compra.id] = novoId
                comprasImportadas++

            }

            val itensRemapeados =
                backup.itensCompra.mapNotNull { item ->

                    val novaCompraId =
                        compraIdMap[item.compraId] ?: return@mapNotNull null

                    val novoProdutoId =
                        produtoIdMap[item.produtoId] ?: return@mapNotNull null

                    item.copy(id = 0, compraId = novaCompraId, produtoId = novoProdutoId)

                }

            if (itensRemapeados.isNotEmpty()) {
                compraDao.insertItens(itensRemapeados)
            }

            var rendasImportadas = 0

            backup.rendas.forEach { renda ->

                val novaCategoriaId =
                    rendaCategoriaIdMap[renda.rendaCategoriaId] ?: return@forEach

                val novaFonteId =
                    fonteRendaIdMap[renda.fonteRendaId] ?: return@forEach

                val novoOrigemId =
                    renda.origemRecorrenteId?.let { rendaIdMap[it] }

                val novoId =
                    rendaDao.insert(
                        renda.copy(
                            id = 0,
                            rendaCategoriaId = novaCategoriaId,
                            fonteRendaId = novaFonteId,
                            origemRecorrenteId = novoOrigemId
                        )
                    )

                rendaIdMap[renda.id] = novoId
                rendasImportadas++

            }

            ImportResult(
                estabelecimentos = estabelecimentoIdMap.size,
                produtos = produtoIdMap.size,
                categorias = categoriaIdMap.size,
                compras = comprasImportadas,
                itensCompra = itensRemapeados.size,
                rendaCategorias = rendaCategoriaIdMap.size,
                fontesRenda = fonteRendaIdMap.size,
                rendas = rendasImportadas
            )

        }

    }

}
