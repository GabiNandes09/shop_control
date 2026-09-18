package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.model.MonthlySpending
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth

private const val MESES_HISTORICO = 6

class GetMonthlySpendingHistoryUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(): Flow<List<MonthlySpending>> =

        repository.getCompras().map { compras ->

            compras
                .mapNotNull { compraCompleta ->

                    parseDataCompra(compraCompleta.compra.dataParaFiltro)?.let { data ->
                        YearMonth.from(data) to compraCompleta.compra.valorTotal
                    }

                }
                .groupBy(
                    keySelector = { it.first },
                    valueTransform = { it.second }
                )
                .map { (yearMonth, valores) ->

                    MonthlySpending(
                        yearMonth = yearMonth,
                        total = valores.sum()
                    )

                }
                .sortedBy { it.yearMonth }
                .takeLast(MESES_HISTORICO)

        }

}
