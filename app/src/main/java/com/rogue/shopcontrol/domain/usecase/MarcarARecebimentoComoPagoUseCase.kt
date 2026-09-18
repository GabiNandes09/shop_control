package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.ARecebimentoRepository
import java.time.LocalDate

class MarcarARecebimentoComoPagoUseCase(
    private val repository: ARecebimentoRepository
) {

    suspend operator fun invoke(
        arecebimentoId: Long,
        dataRendaEscolhida: LocalDate
    ) =
        repository.marcarComoPago(arecebimentoId, dataRendaEscolhida)

}
