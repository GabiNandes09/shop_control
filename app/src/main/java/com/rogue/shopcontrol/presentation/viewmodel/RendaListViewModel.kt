package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.AddFonteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllFontesRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.SaveRendaUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.RendaListState
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.parseDecimalInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class RendaListViewModel(
    getRendaComDados: GetRendaComDadosUseCase,
    getRendaCategorias: GetRendaCategoriasUseCase,
    getAllFontesRenda: GetAllFontesRendaUseCase,
    private val addFonteRenda: AddFonteRendaUseCase,
    private val saveRenda: SaveRendaUseCase,
    private val deleteRenda: DeleteRendaUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase
) : ViewModel() {


    private var itensOriginais: List<RendaComDados> = emptyList()
    private var fontesCache: List<FonteRendaEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            RendaListState()
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

            getRendaComDados().collect { itens ->

                itensOriginais = itens

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getRendaCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(categorias = categorias)

            }

        }

        viewModelScope.launch {

            getAllFontesRenda().collect { fontes ->
                fontesCache = fontes
            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    fun onShowAddDialog() {

        _state.value =
            _state.value.copy(
                showDialog = true,
                isEdicao = false,
                editingId = 0L,
                descricao = "",
                valorInput = "",
                dataSelecionada = LocalDate.now(),
                selectedCategoriaId = _state.value.categorias.firstOrNull()?.id,
                fonteInput = "",
                fontesSugeridas = emptyList(),
                selectedFonteId = null,
                recorrente = false,
                errorRes = null
            )

    }


    fun onItemClick(item: RendaComDados) {

        _state.value =
            _state.value.copy(
                showDialog = true,
                isEdicao = true,
                editingId = item.id,
                descricao = item.descricao,
                valorInput = item.valor.toString(),
                dataSelecionada = parseDataCompra(item.data) ?: LocalDate.now(),
                selectedCategoriaId = item.rendaCategoriaId,
                fonteInput = item.fonteNome,
                fontesSugeridas = emptyList(),
                selectedFonteId = item.fonteRendaId,
                recorrente = item.recorrente,
                errorRes = null
            )

    }


    fun onDismissDialog() {

        _state.value =
            _state.value.copy(showDialog = false)

    }


    fun onDescricaoChanged(texto: String) {

        _state.value =
            _state.value.copy(descricao = texto, errorRes = null)

    }


    fun onValorChanged(texto: String) {

        _state.value =
            _state.value.copy(valorInput = texto, errorRes = null)

    }


    fun onShowDatePicker() {

        _state.value =
            _state.value.copy(showDatePicker = true)

    }


    fun onDismissDatePicker() {

        _state.value =
            _state.value.copy(showDatePicker = false)

    }


    fun onDataChanged(data: LocalDate) {

        _state.value =
            _state.value.copy(dataSelecionada = data, showDatePicker = false)

    }


    fun onShowCategoriaPicker() {

        _state.value =
            _state.value.copy(showCategoriaPicker = true)

    }


    fun onDismissCategoriaPicker() {

        _state.value =
            _state.value.copy(showCategoriaPicker = false)

    }


    fun onCategoriaSelecionada(categoriaId: Long) {

        _state.value =
            _state.value.copy(
                selectedCategoriaId = categoriaId,
                showCategoriaPicker = false
            )

    }


    fun onFonteInputChanged(texto: String) {

        _state.value =
            _state.value.copy(
                fonteInput = texto,
                selectedFonteId = null,
                fontesSugeridas = if (texto.isBlank()) {
                    emptyList()
                } else {
                    fontesCache.filter {
                        it.nome.contains(texto, ignoreCase = true)
                    }.take(5)
                },
                errorRes = null
            )

    }


    fun onFonteSugestaoSelecionada(fonte: FonteRendaEntity) {

        _state.value =
            _state.value.copy(
                fonteInput = fonte.nome,
                selectedFonteId = fonte.id,
                fontesSugeridas = emptyList()
            )

    }


    fun onRecorrenteChanged(valor: Boolean) {

        _state.value =
            _state.value.copy(recorrente = valor)

    }


    fun onShowDeleteConfirm() {

        _state.value =
            _state.value.copy(showDeleteConfirm = true)

    }


    fun onDismissDeleteConfirm() {

        _state.value =
            _state.value.copy(showDeleteConfirm = false)

    }


    fun onConfirmDelete() {

        val estadoAtual = _state.value

        viewModelScope.launch {

            deleteRenda(estadoAtual.editingId)

            _state.value =
                _state.value.copy(
                    showDeleteConfirm = false,
                    showDialog = false
                )

        }

    }


    fun onSave() {

        val estadoAtual = _state.value

        val valor = parseDecimalInput(estadoAtual.valorInput)
        val categoriaId = estadoAtual.selectedCategoriaId

        if (estadoAtual.descricao.isBlank() ||
            valor == null || valor <= 0.0 ||
            categoriaId == null ||
            estadoAtual.fonteInput.isBlank()
        ) {

            _state.value =
                estadoAtual.copy(errorRes = R.string.renda_invalid_error)

            return

        }

        viewModelScope.launch {

            val fonteId =
                estadoAtual.selectedFonteId
                    ?: addFonteRenda(estadoAtual.fonteInput)

            saveRenda(
                rendaId = estadoAtual.editingId,
                descricao = estadoAtual.descricao,
                valor = valor,
                data = estadoAtual.dataSelecionada,
                categoriaId = categoriaId,
                fonteId = fonteId,
                recorrente = estadoAtual.recorrente
            )

            _state.value =
                _state.value.copy(showDialog = false)

        }

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange

        val filtrados =
            itensOriginais.filter { item ->

                val data =
                    parseDataCompra(item.data)

                data != null && data in range

            }

        _state.value =
            _state.value.copy(
                itens = filtrados,
                isLoading = false
            )

    }

}
