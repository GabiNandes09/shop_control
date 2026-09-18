package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.CompraDao
import com.rogue.shopcontrol.data.local.dao.EstabelecimentoDao
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraComData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import kotlinx.coroutines.flow.Flow

class EstabelecimentoRepository(
    private val estabelecimentoDao: EstabelecimentoDao,
    private val compraDao: CompraDao
) {

    fun getComprasComEstabelecimento(): Flow<List<EstabelecimentoCompraComData>> =
        estabelecimentoDao.getComprasComEstabelecimento()


    fun getAll(): Flow<List<EstabelecimentoEntity>> =
        estabelecimentoDao.getAll()


    fun findById(
        estabelecimentoId: Long
    ): Flow<EstabelecimentoEntity?> =
        estabelecimentoDao.findById(estabelecimentoId)


    fun getComprasPorEstabelecimento(
        estabelecimentoId: Long
    ): Flow<List<EstabelecimentoCompraData>> =
        estabelecimentoDao.getComprasPorEstabelecimento(estabelecimentoId)


    suspend fun updateApelido(
        estabelecimentoId: Long,
        apelido: String?
    ) =
        estabelecimentoDao.updateApelido(
            estabelecimentoId,
            apelido?.let { capitalizarPrimeiraLetra(it) }
        )


    suspend fun linkToEstabelecimentoComCnpj(
        semCnpjId: Long,
        comCnpjId: Long
    ) =
        estabelecimentoDao.linkToEstabelecimentoComCnpj(semCnpjId, comCnpjId)


    suspend fun addEstabelecimento(
        nome: String,
        cnpj: String,
        endereco: String,
        apelido: String?,
        categoriaId: Long?
    ): Long {

        val nomeTrim = capitalizarPrimeiraLetra(nome)
        val cnpjTrim = cnpj.trim()

        val existenteId =
            if (cnpjTrim.isNotBlank()) {
                estabelecimentoDao.findByCnpj(cnpjTrim)?.id
            } else {
                estabelecimentoDao.findByNome(nomeTrim)?.id
            }

        return existenteId
            ?: estabelecimentoDao.insert(
                EstabelecimentoEntity(
                    nome = nomeTrim,
                    cnpj = cnpjTrim,
                    endereco = capitalizarPrimeiraLetra(endereco),
                    apelido = apelido?.trim()?.ifBlank { null }?.let { capitalizarPrimeiraLetra(it) },
                    categoriaId = categoriaId
                )
            )

    }


    suspend fun updateCategoria(
        estabelecimentoId: Long,
        categoriaId: Long?
    ) {

        estabelecimentoDao.updateCategoria(estabelecimentoId, categoriaId)

        if (categoriaId != null) {
            compraDao.backfillCategoriaForEstabelecimento(estabelecimentoId, categoriaId)
        }

    }

}
