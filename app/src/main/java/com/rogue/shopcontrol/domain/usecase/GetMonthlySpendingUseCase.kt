package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth

class GetMonthlySpendingUseCase(
    private val repository: CompraRepository
) {

    operator fun invoke(): Flow<Double> =

        repository.getCompras().map { compras ->

            val mesAtual = YearMonth.now()

            compras
                .filter { compraCompleta ->

                    val data =
                        parseDataCompra(compraCompleta.compra.dataCompra)

                    data != null && YearMonth.from(data) == mesAtual

                }
                .sumOf { it.compra.valorTotal }

        }

}
