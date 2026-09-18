package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository

class AddEstabelecimentoUseCase(
    private val repository: EstabelecimentoRepository
) {

    suspend operator fun invoke(
        nome: String,
        cnpj: String,
        endereco: String,
        apelido: String?,
        categoriaId: Long? = null
    ): Long =
        repository.addEstabelecimento(nome, cnpj, endereco, apelido, categoriaId)

}
