package com.rogue.shopcontrol.domain.model

import java.time.YearMonth

data class BalancoMensal(
    val yearMonth: YearMonth,
    val renda: Double,
    val despesas: Double
) {

    val saldo: Double
        get() = renda - despesas

}
