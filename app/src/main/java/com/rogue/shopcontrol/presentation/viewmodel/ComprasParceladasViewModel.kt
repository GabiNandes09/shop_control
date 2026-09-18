package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ComprasParceladasState
import com.rogue.shopcontrol.presentation.viewmodel.states.SerieParcelada
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

class ComprasParceladasViewModel(
    getCompras: GetComprasUseCase,
    getCategorias: GetCategoriasUseCase
) : ViewModel() {


    private var categoriasCache: List<CategoriaEntity> = emptyList()
    private var comprasParceladasOriginais: List<CompraEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            ComprasParceladasState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                categoriasCache = categorias

                montarSeries()

            }

        }

        viewModelScope.launch {

            getCompras().collect { compras ->

                comprasParceladasOriginais =
                    compras
                        .map { it.compra }
                        .filter { it.tipo == TipoCompra.PARCELADA }

                montarSeries()

            }

        }

    }


    fun onToggleConcluidas() {

        _state.value =
            _state.value.copy(mostrarConcluidas = !_state.value.mostrarConcluidas)

    }


    private fun montarSeries() {

        val porId = comprasParceladasOriginais.associateBy { it.id }

        val hoje = YearMonth.now()

        val series =
            comprasParceladasOriginais
                .groupBy { encontrarRaizId(it, porId) }
                .map { (_, grupo) ->

                    val ordenado =
                        grupo.sortedBy { parseDataCompra(it.dataCompetencia) }

                    val atual =
                        ordenado.lastOrNull { parcela ->

                            val mes =
                                parseDataCompra(parcela.dataCompetencia)?.let { YearMonth.from(it) }

                            mes != null && !mes.isAfter(hoje)

                        } ?: ordenado.first()

                    val numero = ordenado.indexOf(atual) + 1
                    val total = atual.totalParcelas ?: ordenado.size

                    val ultimaCompetencia =
                        ordenado.lastOrNull()
                            ?.dataCompetencia
                            ?.let { parseDataCompra(it) }
                            ?.let { YearMonth.from(it) }

                    val concluida =
                        ultimaCompetencia != null && ultimaCompetencia.isBefore(hoje)

                    SerieParcelada(
                        compraIdAtual = atual.id,
                        nome = atual.nome ?: "",
                        categoriaNome = atual.categoriaId?.let { categoriaId ->
                            categoriasCache.firstOrNull { it.id == categoriaId }?.nome
                        },
                        valorParcela = atual.valorTotal,
                        parcelaAtualNumero = numero,
                        totalParcelas = total,
                        concluida = concluida
                    )

                }

        _state.value =
            _state.value.copy(
                ativas = series.filterNot { it.concluida }.sortedByDescending { it.valorParcela },
                concluidas = series.filter { it.concluida },
                isLoading = false
            )

    }


    private fun encontrarRaizId(
        compra: CompraEntity,
        porId: Map<Long, CompraEntity>
    ): Long {

        var atual = compra

        while (atual.origemRecorrenteId != null) {
            atual = porId[atual.origemRecorrenteId] ?: break
        }

        return atual.id

    }

}
