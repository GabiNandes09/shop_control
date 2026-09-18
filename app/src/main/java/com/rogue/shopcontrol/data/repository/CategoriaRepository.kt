package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.CategoriaDao
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(
    private val categoriaDao: CategoriaDao
) {

    fun getCategorias(): Flow<List<CategoriaEntity>> =
        categoriaDao.getAll()


    suspend fun addCategoria(
        nome: String
    ): Boolean {

        val nomeTrimmed = capitalizarPrimeiraLetra(nome)

        if (nomeTrimmed.isBlank()) {
            return false
        }

        val existente =
            categoriaDao.findByName(nomeTrimmed)

        if (existente != null) {
            return false
        }

        categoriaDao.insert(
            CategoriaEntity(
                nome = nomeTrimmed
            )
        )

        return true

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
            categoriaDao.findByName(nomeTrimmed)

        if (existente != null && existente.id != categoriaId) {
            return false
        }

        categoriaDao.updateNome(categoriaId, nomeTrimmed)

        return true

    }


    suspend fun deleteCategoria(
        categoriaId: Long
    ): Boolean {

        val emUso =
            categoriaDao.countUsage(categoriaId) > 0

        if (emUso) {
            return false
        }

        categoriaDao.delete(categoriaId)

        return true

    }


    suspend fun addCategoriaERetornarId(
        nome: String
    ): Long {

        val nomeTrimmed = capitalizarPrimeiraLetra(nome)

        return categoriaDao.findByName(nomeTrimmed)?.id
            ?: categoriaDao.insert(CategoriaEntity(nome = nomeTrimmed))

    }


    suspend fun updateGrupo(
        categoriaId: Long,
        grupoId: Long?
    ) =
        categoriaDao.updateGrupo(categoriaId, grupoId)

}
