package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import com.rogue.shopcontrol.domain.model.MonthlySpending
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth

private const val MESES_HISTORICO = 6

class GetEstabelecimentoMonthlyHistoryUseCase(
    private val repository: EstabelecimentoRepository
) {

    operator fun invoke(estabelecimentoId: Long): Flow<List<MonthlySpending>> =

        repository.getComprasPorEstabelecimento(estabelecimentoId).map { compras ->

            compras
                .mapNotNull { compra ->

                    parseDataCompra(compra.dataCompra)?.let { data ->
                        YearMonth.from(data) to compra.valorTotal
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
