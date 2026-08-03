package com.rogue.shopcontrol.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
