package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.HomeDateRangeRepository
import com.rogue.shopcontrol.domain.model.DateRange

class SaveHomeDateRangeUseCase(
    private val repository: HomeDateRangeRepository
) {

    suspend operator fun invoke(range: DateRange) =
        repository.save(range)

}
