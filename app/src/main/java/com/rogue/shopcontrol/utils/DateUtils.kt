package com.rogue.shopcontrol.utils

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

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


fun formatMonthYear(yearMonth: YearMonth): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "MMMM yyyy",
            Locale.getDefault()
        )

    return formatter.format(yearMonth)
        .replaceFirstChar {
            it.titlecase(Locale.getDefault())
        }

}
