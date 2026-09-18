package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.utils.parseDecimalInput
import java.time.LocalDate

enum class CampoValorAtivo {
    UNITARIO,
    TOTAL
}

data class ManualPurchaseItemDraft(
    val produtoId: Long,
    val produtoNome: String,
    val quantidade: Double,
    val valorUnitario: Double,
    val valorTotal: Double
)

data class DivisaoParticipanteDraft(
    val fonteId: Long,
    val nome: String,
    val valorInput: String
) {

    val valor: Double
        get() = parseDecimalInput(valorInput) ?: 0.0

}

data class ManualPurchaseEntryState(
    val isEdicao: Boolean = false,
    val isLoading: Boolean = false,

    val tipoCompra: TipoCompra = TipoCompra.VARIAVEL,

    val estabelecimentoNome: String = "",
    val estabelecimentoApelido: String = "",
    val estabelecimentoExistente: Boolean = false,
    val estabelecimentosSugeridos: List<EstabelecimentoEntity> = emptyList(),

    val dataCompra: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false,

    val itens: List<ManualPurchaseItemDraft> = emptyList(),

    val showAddItemDialog: Boolean = false,
    val itemNomeInput: String = "",
    val produtosSugeridos: List<ProdutoEntity> = emptyList(),
    val itemQuantidade: String = "",
    val itemValorUnitario: String = "",
    val itemValorTotal: String = "",
    val campoValorAtivo: CampoValorAtivo = CampoValorAtivo.UNITARIO,
    val itemErrorRes: Int? = null,

    val showConfirmNewProduct: Boolean = false,
    val pendingNovoProdutoNome: String = "",

    val nomeCompra: String = "",
    val valorCompraInput: String = "",
    val categorias: List<CategoriaEntity> = emptyList(),
    val selectedCategoriaId: Long? = null,
    val showCategoriaPicker: Boolean = false,
    val totalParcelasInput: String = "",
    val parcelasJaPagasInput: String = "0",

    val dividirConta: Boolean = false,
    val dividirIgualmente: Boolean = true,
    val participantes: List<DivisaoParticipanteDraft> = emptyList(),
    val participanteInput: String = "",
    val participantesSugeridos: List<FonteRendaEntity> = emptyList(),
    val divisaoErrorRes: Int? = null,

    val isSaving: Boolean = false,
    val savedCompraId: Long? = null
) {

    val valorTotalCompra: Double
        get() = itens.sumOf { it.valorTotal }

    val valorTotalPago: Double
        get() = if (tipoCompra == TipoCompra.VARIAVEL) {
            valorTotalCompra
        } else {
            parseDecimalInput(valorCompraInput) ?: 0.0
        }

    val somaParticipantes: Double
        get() = participantes.sumOf { it.valor }

    val suaParte: Double
        get() = valorTotalPago - somaParticipantes

    private val divisaoValida: Boolean
        get() = !dividirConta || (participantes.isNotEmpty() && somaParticipantes <= valorTotalPago)

    val podeSalvar: Boolean
        get() = when (tipoCompra) {

            TipoCompra.VARIAVEL ->
                estabelecimentoNome.isNotBlank() && itens.isNotEmpty() && divisaoValida

            TipoCompra.FIXA ->
                estabelecimentoNome.isNotBlank() &&
                    nomeCompra.isNotBlank() &&
                    valorCompraInput.isNotBlank() &&
                    selectedCategoriaId != null &&
                    divisaoValida

            TipoCompra.PARCELADA ->
                estabelecimentoNome.isNotBlank() &&
                    nomeCompra.isNotBlank() &&
                    valorCompraInput.isNotBlank() &&
                    selectedCategoriaId != null &&
                    totalParcelasInput.isNotBlank() &&
                    divisaoValida

            TipoCompra.RAPIDA ->
                estabelecimentoNome.isNotBlank() &&
                    nomeCompra.isNotBlank() &&
                    valorCompraInput.isNotBlank() &&
                    selectedCategoriaId != null &&
                    divisaoValida

        }

}
