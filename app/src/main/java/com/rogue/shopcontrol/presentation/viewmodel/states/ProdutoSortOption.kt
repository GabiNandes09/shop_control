package com.rogue.shopcontrol.presentation.viewmodel.states

import androidx.annotation.StringRes
import com.rogue.shopcontrol.R

enum class ProdutoSortOption(
    @param:StringRes val labelRes: Int
) {
    VALOR_GASTO(R.string.spending_amount_title),
    QUANTIDADE(R.string.sort_option_quantity)
}
