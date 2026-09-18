package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraComData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetTodasComprasComEstabelecimentoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.AnaliseState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TOP_LIMITE = 5

class AnaliseViewModel(
    getProdutoItensComData: GetProdutoItensComDataUseCase,
    getTodasComprasComEstabelecimento: GetTodasComprasComEstabelecimentoUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase
) : ViewModel() {


    private var itensOriginais: List<ProdutoItemComData> = emptyList()
    private var comprasEstabelecimentoOriginais: List<EstabelecimentoCompraComData> = emptyList()

    private val _state =
        MutableStateFlow(
            AnaliseState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(dateRange = getHomeDateRange().first())

            aplicarFiltro()

        }

        viewModelScope.launch {

            getProdutoItensComData().collect { itens ->

                itensOriginais = itens

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getTodasComprasComEstabelecimento().collect { compras ->

                comprasEstabelecimentoOriginais = compras

                aplicarFiltro()

            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange

        val itensFiltrados =
            itensOriginais.filter { item ->

                val data =
                    parseDataCompra(item.dataCompra)

                data != null && data in range

            }

        val topProdutos =
            itensFiltrados
                .groupBy { it.produtoId to it.nomeProduto }
                .map { (chave, itens) ->

                    val primeiro = itens.first()

                    ProdutoGasto(
                        id = chave.first,
                        nome = chave.second,
                        valorTotalGasto = itens.sumOf { it.valorTotal },
                        quantidadeTotal = itens.sumOf { it.quantidade },
                        categoriaId = primeiro.categoriaId,
                        categoriaNome = primeiro.categoriaNome,
                        apelido = primeiro.apelidoProduto,
                        codigoBarras = primeiro.codigoBarras
                    )

                }
                .sortedByDescending { it.valorTotalGasto }
                .take(TOP_LIMITE)

        val comprasEstabelecimentoFiltradas =
            comprasEstabelecimentoOriginais.filter { compra ->

                val data =
                    parseDataCompra(compra.dataParaFiltro)

                data != null && data in range

            }

        val topEstabelecimentos =
            comprasEstabelecimentoFiltradas
                .groupBy { it.estabelecimentoId }
                .map { (id, compras) ->

                    val primeira = compras.first()

                    EstabelecimentoGasto(
                        id = id,
                        nome = primeira.nome,
                        apelido = primeira.apelido,
                        valorTotalGasto = compras.sumOf { it.valorTotal }
                    )

                }
                .sortedByDescending { it.valorTotalGasto }
                .take(TOP_LIMITE)

        _state.value =
            _state.value.copy(
                topProdutos = topProdutos,
                topEstabelecimentos = topEstabelecimentos,
                isLoading = false
            )

    }

}
