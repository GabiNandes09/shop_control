package com.rogue.shopcontrol.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
        .format(value)

fun parseDecimalInput(texto: String): Double? =
    texto.trim().replace(",", ".").toDoubleOrNull()

fun formatDecimalInput(value: Double): String =
    String.format(Locale.US, "%.2f", value)


fun capitalizarPrimeiraLetra(texto: String): String {

    val trimmed = texto.trim()

    if (trimmed.isEmpty()) {
        return trimmed
    }

    return trimmed.replaceFirstChar { it.titlecase(Locale.forLanguageTag("pt-BR")) }

}
