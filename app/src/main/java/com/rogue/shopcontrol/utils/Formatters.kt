package com.rogue.shopcontrol.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
        .format(value)
