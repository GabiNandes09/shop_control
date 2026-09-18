package com.rogue.shopcontrol.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class DateRange(
    val start: LocalDate,
    val end: LocalDate
) {

    operator fun contains(data: LocalDate): Boolean =
        !data.isBefore(start) && !data.isAfter(end)

    companion object {

        fun currentMonth(): DateRange {

            val mesAtual = YearMonth.now()

            return DateRange(
                start = mesAtual.atDay(1),
                end = mesAtual.atEndOfMonth()
            )

        }

    }

}
