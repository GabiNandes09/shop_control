package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.CategoriaDao
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(
    private val categoriaDao: CategoriaDao
) {

    fun getCategorias(): Flow<List<CategoriaEntity>> =
        categoriaDao.getAll()


    suspend fun addCategoria(
        nome: String
    ): Boolean {

        val nomeTrimmed = nome.trim()

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


    suspend fun deleteCategoria(
        categoriaId: Long
    ) =
        categoriaDao.delete(categoriaId)

}
