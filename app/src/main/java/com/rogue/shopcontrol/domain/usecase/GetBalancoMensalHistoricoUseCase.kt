package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.data.repository.RendaRepository
import com.rogue.shopcontrol.domain.model.BalancoMensal
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.YearMonth

private const val MESES_HISTORICO = 12

class GetBalancoMensalHistoricoUseCase(
    private val compraRepository: CompraRepository,
    private val rendaRepository: RendaRepository
) {

    operator fun invoke(): Flow<List<BalancoMensal>> =

        combine(
            compraRepository.getCompras(),
            rendaRepository.getRendaComDados()
        ) { compras, rendas ->

            val despesasPorMes =
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
                    .mapValues { (_, valores) -> valores.sum() }

            val rendaPorMes =
                rendas
                    .mapNotNull { renda ->

                        parseDataCompra(renda.data)?.let { data ->
                            YearMonth.from(data) to renda.valor
                        }

                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                    .mapValues { (_, valores) -> valores.sum() }

            (despesasPorMes.keys + rendaPorMes.keys)
                .distinct()
                .sorted()
                .takeLast(MESES_HISTORICO)
                .map { mes ->

                    BalancoMensal(
                        yearMonth = mes,
                        renda = rendaPorMes[mes] ?: 0.0,
                        despesas = despesasPorMes[mes] ?: 0.0
                    )

                }

        }

}
