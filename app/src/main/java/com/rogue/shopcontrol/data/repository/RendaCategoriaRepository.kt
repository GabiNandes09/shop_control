package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.RendaCategoriaDao
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import kotlinx.coroutines.flow.Flow

class RendaCategoriaRepository(
    private val rendaCategoriaDao: RendaCategoriaDao
) {

    fun getCategorias(): Flow<List<RendaCategoriaEntity>> =
        rendaCategoriaDao.getAll()


    suspend fun addCategoria(
        nome: String
    ): Boolean {

        val nomeTrimmed = capitalizarPrimeiraLetra(nome)

        if (nomeTrimmed.isBlank()) {
            return false
        }

        val existente =
            rendaCategoriaDao.findByName(nomeTrimmed)

        if (existente != null) {
            return false
        }

        rendaCategoriaDao.insert(
            RendaCategoriaEntity(
                nome = nomeTrimmed
            )
        )

        return true

    }


    suspend fun addCategoriaERetornarId(
        nome: String
    ): Long {

        val nomeTrimmed = capitalizarPrimeiraLetra(nome)

        return rendaCategoriaDao.findByName(nomeTrimmed)?.id
            ?: rendaCategoriaDao.insert(
                RendaCategoriaEntity(
                    nome = nomeTrimmed
                )
            )

    }


    suspend fun updateCategoria(
        categoriaId: Long,
        nome: String
    ): Boolean {

        val nomeTrimmed = capitalizarPrimeiraLetra(nome)

        if (nomeTrimmed.isBlank()) {
            return false
        }

        val existente =
            rendaCategoriaDao.findByName(nomeTrimmed)

        if (existente != null && existente.id != categoriaId) {
            return false
        }

        rendaCategoriaDao.updateNome(categoriaId, nomeTrimmed)

        return true

    }


    suspend fun deleteCategoria(
        categoriaId: Long
    ): Boolean {

        val emUso =
            rendaCategoriaDao.countUsage(categoriaId) > 0

        if (emUso) {
            return false
        }

        rendaCategoriaDao.delete(categoriaId)

        return true

    }

}
