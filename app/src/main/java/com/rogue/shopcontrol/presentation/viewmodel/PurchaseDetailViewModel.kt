package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.ItemPrecoHistorico
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.domain.usecase.DeleteCompraForwardUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetDivisaoContaUseCase
import com.rogue.shopcontrol.domain.usecase.GetParcelasDaSerieUseCase
import com.rogue.shopcontrol.domain.usecase.GetTodosPrecosProdutoUseCase
import com.rogue.shopcontrol.presentation.model.PriceAlert
import com.rogue.shopcontrol.presentation.viewmodel.states.PurchaseDetailState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class PurchaseDetailViewModel(
    private val compraId: Long,
    getCompraById: GetCompraByIdUseCase,
    getTodosPrecosProduto: GetTodosPrecosProdutoUseCase,
    private val deleteCompra: DeleteCompraUseCase,
    private val deleteCompraForward: DeleteCompraForwardUseCase,
    private val getParcelasDaSerie: GetParcelasDaSerieUseCase,
    getCategorias: GetCategoriasUseCase,
    private val getDivisaoConta: GetDivisaoContaUseCase
) : ViewModel() {


    private var categoriasCache: List<CategoriaEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            PurchaseDetailState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCategorias().collect { categorias ->
                categoriasCache = categorias
            }

        }

        viewModelScope.launch {

            combine(
                getCompraById(compraId),
                getTodosPrecosProduto()
            ) { compra, historico ->
                compra to historico
            }.collect { (compra, historico) ->

                if (!_state.value.isDeleted) {

                    _state.value = _state.value.copy(
                        compra = compra,
                        precoAlerts = if (compra != null) {
                            calcularAlertasPreco(compra, historico)
                        } else {
                            emptyMap()
                        },
                        categoriaNome = compra?.compra?.categoriaId?.let { categoriaId ->
                            categoriasCache.firstOrNull { it.id == categoriaId }?.nome
                        },
                        isLoading = false
                    )

                    if (compra?.compra?.tipo == TipoCompra.PARCELADA) {

                        _state.value =
                            _state.value.copy(
                                parcelas = getParcelasDaSerie(compraId)
                            )

                    }

                }

            }

        }

        viewModelScope.launch {

            getDivisaoConta(compraId).collect { participantes ->

                _state.value =
                    _state.value.copy(divisaoParticipantes = participantes)

            }

        }

    }


    fun delete() {

        viewModelScope.launch {

            val tipo = _state.value.compra?.compra?.tipo

            if (tipo == TipoCompra.FIXA || tipo == TipoCompra.PARCELADA) {
                deleteCompraForward(compraId)
            } else {
                deleteCompra(compraId)
            }

            _state.value =
                _state.value.copy(
                    isDeleted = true
                )

        }

    }


    private fun calcularAlertasPreco(
        compra: CompraCompleta,
        historico: List<ItemPrecoHistorico>
    ): Map<Long, PriceAlert> {

        val dataAtual =
            parseDataCompra(compra.compra.dataCompra) ?: return emptyMap()

        val alerts = mutableMapOf<Long, PriceAlert>()

        compra.itens.forEach { itemCompleto ->

            val valorAnterior =
                precoAnterior(
                    produtoId = itemCompleto.produto.id,
                    compraAtualId = compra.compra.id,
                    dataAtual = dataAtual,
                    historico = historico
                )

            if (valorAnterior != null && valorAnterior != itemCompleto.item.valorUnitario) {

                alerts[itemCompleto.item.id] = PriceAlert(
                    diferenca = kotlin.math.abs(itemCompleto.item.valorUnitario - valorAnterior),
                    maisCaro = itemCompleto.item.valorUnitario > valorAnterior
                )

            }

        }

        return alerts

    }


    private fun precoAnterior(
        produtoId: Long,
        compraAtualId: Long,
        dataAtual: LocalDate,
        historico: List<ItemPrecoHistorico>
    ): Double? {

        return historico
            .asSequence()
            .filter { it.produtoId == produtoId && it.compraId != compraAtualId }
            .mapNotNull { item ->

                val data = parseDataCompra(item.dataCompra) ?: return@mapNotNull null

                Triple(data, item.compraId, item.valorUnitario)

            }
            .filter { (data, id, _) ->
                data.isBefore(dataAtual) || (data == dataAtual && id < compraAtualId)
            }
            .maxWithOrNull(
                compareBy(
                    { it.first },
                    { it.second }
                )
            )
            ?.third

    }

}
