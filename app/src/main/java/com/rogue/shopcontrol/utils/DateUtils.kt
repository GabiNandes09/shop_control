package com.rogue.shopcontrol.utils

import com.rogue.shopcontrol.domain.model.DateRange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val DATE_RANGE_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

private val DATA_COMPRA_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun parseDataCompra(dataCompra: String?): LocalDate? {

    if (dataCompra.isNullOrBlank()) {
        return null
    }

    return try {

        LocalDate.parse(
            dataCompra,
            DATA_COMPRA_FORMATTER
        )

    } catch (e: DateTimeParseException) {

        null

    }

}


fun formatDateRange(dateRange: DateRange): String =
    "${dateRange.start.format(DATE_RANGE_DISPLAY_FORMATTER)} - ${dateRange.end.format(DATE_RANGE_DISPLAY_FORMATTER)}"


fun formatDataCompra(data: LocalDate, hora: LocalTime = LocalTime.NOON): String =
    LocalDateTime.of(data, hora).format(DATA_COMPRA_FORMATTER)


fun LocalDate.plusMonthsClamped(months: Long): LocalDate {

    val mesDestino =
        YearMonth.from(this).plusMonths(months)

    val dia =
        minOf(dayOfMonth, mesDestino.lengthOfMonth())

    return mesDestino.atDay(dia)

}
