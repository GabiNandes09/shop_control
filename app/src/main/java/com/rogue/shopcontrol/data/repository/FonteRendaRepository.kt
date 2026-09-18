package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.FonteRendaDao
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import kotlinx.coroutines.flow.Flow

class FonteRendaRepository(
    private val fonteRendaDao: FonteRendaDao
) {

    fun getAll(): Flow<List<FonteRendaEntity>> =
        fonteRendaDao.getAll()


    fun findById(
        fonteId: Long
    ): Flow<FonteRendaEntity?> =
        fonteRendaDao.findById(fonteId)


    suspend fun addFonte(
        nome: String
    ): Long {

        val nomeTrim = capitalizarPrimeiraLetra(nome)

        return fonteRendaDao.findByNome(nomeTrim)?.id
            ?: fonteRendaDao.insert(
                FonteRendaEntity(nome = nomeTrim)
            )

    }

}
