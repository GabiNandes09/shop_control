package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.repository.FonteRendaRepository
import kotlinx.coroutines.flow.Flow

class GetAllFontesRendaUseCase(
    private val repository: FonteRendaRepository
) {

    operator fun invoke(): Flow<List<FonteRendaEntity>> =
        repository.getAll()

}
