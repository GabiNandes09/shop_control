package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.EstabelecimentoDao
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import kotlinx.coroutines.flow.Flow

class EstabelecimentoRepository(
    private val estabelecimentoDao: EstabelecimentoDao
) {

    fun getEstabelecimentosGasto(): Flow<List<EstabelecimentoGasto>> =
        estabelecimentoDao.getEstabelecimentosGasto()


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
        estabelecimentoDao.updateApelido(estabelecimentoId, apelido)

}
