package com.rogue.shopcontrol.domain.usecase

import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.domain.model.NfceData

class SavePurchaseUseCase(
    private val repository: CompraRepository
) {


    suspend operator fun invoke(
        nfce: NfceData
    ) {

        repository.save(nfce)

    }

}