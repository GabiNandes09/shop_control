package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.repository.FonteRendaRepository
import kotlinx.coroutines.flow.Flow

class GetFonteRendaByIdUseCase(
    private val repository: FonteRendaRepository
) {

    operator fun invoke(fonteId: Long): Flow<FonteRendaEntity?> =
        repository.findById(fonteId)

}
