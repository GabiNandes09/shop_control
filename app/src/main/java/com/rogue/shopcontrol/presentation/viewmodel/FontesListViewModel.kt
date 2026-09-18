package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.usecase.AddFonteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllFontesRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaComDadosUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.FonteRendaTotal
import com.rogue.shopcontrol.presentation.viewmodel.states.FontesListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FontesListViewModel(
    getAllFontesRenda: GetAllFontesRendaUseCase,
    getRendaComDados: GetRendaComDadosUseCase,
    private val addFonteRenda: AddFonteRendaUseCase
) : ViewModel() {


    private var fontesOriginais: List<FonteRendaEntity> = emptyList()
    private var rendaOriginais: List<RendaComDados> = emptyList()

    private val _state =
        MutableStateFlow(
            FontesListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getAllFontesRenda().collect { fontes ->

                fontesOriginais = fontes

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getRendaComDados().collect { renda ->

                rendaOriginais = renda

                aplicarFiltro()

            }

        }

    }


    fun onNameFilterChanged(query: String) {

        _state.value =
            _state.value.copy(nameFilter = query)

        aplicarFiltro()

    }


    fun onShowAddDialog() {

        _state.value =
            _state.value.copy(
                showAddDialog = true,
                newNome = "",
                errorRes = null
            )

    }


    fun onDismissAddDialog() {

        _state.value =
            _state.value.copy(showAddDialog = false)

    }


    fun onNewNomeChanged(texto: String) {

        _state.value =
            _state.value.copy(newNome = texto, errorRes = null)

    }


    fun onSaveNewFonte() {

        val estadoAtual = _state.value

        if (estadoAtual.newNome.isBlank()) {

            _state.value =
                estadoAtual.copy(errorRes = R.string.fonte_name_required_error)

            return

        }

        viewModelScope.launch {

            addFonteRenda(estadoAtual.newNome)

            _state.value =
                _state.value.copy(showAddDialog = false)

        }

    }


    private fun aplicarFiltro() {

        val filtroNome = _state.value.nameFilter

        val totalPorFonte =
            rendaOriginais
                .groupBy { it.fonteRendaId }
                .mapValues { (_, itens) -> itens.sumOf { it.valor } }

        val agregados =
            fontesOriginais
                .map { fonte ->

                    FonteRendaTotal(
                        id = fonte.id,
                        nome = fonte.nome,
                        valorTotalRecebido = totalPorFonte[fonte.id] ?: 0.0
                    )

                }
                .filter { fonte ->

                    filtroNome.isBlank() ||
                        fonte.nome.contains(filtroNome, ignoreCase = true)

                }
                .sortedByDescending { it.valorTotalRecebido }

        _state.value =
            _state.value.copy(
                fontes = agregados,
                isLoading = false
            )

    }

}
