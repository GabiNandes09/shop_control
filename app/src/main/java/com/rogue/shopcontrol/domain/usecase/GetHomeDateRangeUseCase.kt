package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.HomeDateRangeRepository
import com.rogue.shopcontrol.domain.model.DateRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHomeDateRangeUseCase(
    private val repository: HomeDateRangeRepository
) {

    operator fun invoke(): Flow<DateRange> =
        repository.observe().map { it ?: DateRange.currentMonth() }

}
