package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.domain.usecase.AddFonteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteARecebimentoForwardUseCase
import com.rogue.shopcontrol.domain.usecase.GetARecebimentosComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllFontesRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.MarcarARecebimentoComoPagoUseCase
import com.rogue.shopcontrol.domain.usecase.SaveARecebimentoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ARecebimentoListState
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.parseDecimalInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class ARecebimentoListViewModel(
    private val highlightId: Long,
    getARecebimentosComDados: GetARecebimentosComDadosUseCase,
    getRendaCategorias: GetRendaCategoriasUseCase,
    getAllFontesRenda: GetAllFontesRendaUseCase,
    private val addFonteRenda: AddFonteRendaUseCase,
    private val saveARecebimento: SaveARecebimentoUseCase,
    private val deleteARecebimentoForward: DeleteARecebimentoForwardUseCase,
    private val marcarComoPago: MarcarARecebimentoComoPagoUseCase
) : ViewModel() {


    private var fontesCache: List<FonteRendaEntity> = emptyList()
    private var highlightHandled = false

    private val _state =
        MutableStateFlow(
            ARecebimentoListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getARecebimentosComDados().collect { itens ->

                _state.value =
                    _state.value.copy(itens = itens, isLoading = false)

                if (!highlightHandled && highlightId != 0L) {

                    itens.firstOrNull { it.id == highlightId }?.let { item ->
                        highlightHandled = true
                        onItemClick(item)
                    }

                }

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


    fun onShowAddDialog() {

        _state.value =
            _state.value.copy(
                showDialog = true,
                isEdicao = false,
                editingId = 0L,
                editingPago = false,
                editingTipo = TipoARecebimento.PONTUAL,
                descricao = "",
                valorInput = "",
                dataSelecionada = LocalDate.now(),
                selectedCategoriaId = _state.value.categorias.firstOrNull()?.id,
                fonteInput = "",
                fontesSugeridas = emptyList(),
                selectedFonteId = null,
                tipo = TipoARecebimento.PONTUAL,
                totalParcelasInput = "",
                errorRes = null
            )

    }


    fun onItemClick(item: ARecebimentoComDados) {

        _state.value =
            _state.value.copy(
                showDialog = true,
                isEdicao = true,
                editingId = item.id,
                editingPago = item.pago,
                editingTipo = item.tipo,
                descricao = item.descricao,
                valorInput = item.valor.toString(),
                dataSelecionada = parseDataCompra(item.dataPrevista) ?: LocalDate.now(),
                selectedCategoriaId = item.rendaCategoriaId,
                fonteInput = item.fonteNome,
                fontesSugeridas = emptyList(),
                selectedFonteId = item.fonteRendaId,
                tipo = item.tipo,
                totalParcelasInput = item.totalParcelas?.toString() ?: "",
                errorRes = null
            )

    }


    fun onDismissDialog() {
        _state.value = _state.value.copy(showDialog = false)
    }


    fun onDescricaoChanged(texto: String) {
        _state.value = _state.value.copy(descricao = texto, errorRes = null)
    }


    fun onValorChanged(texto: String) {
        _state.value = _state.value.copy(valorInput = texto, errorRes = null)
    }


    fun onShowDatePicker() {
        _state.value = _state.value.copy(showDatePicker = true)
    }


    fun onDismissDatePicker() {
        _state.value = _state.value.copy(showDatePicker = false)
    }


    fun onDataChanged(data: LocalDate) {
        _state.value = _state.value.copy(dataSelecionada = data, showDatePicker = false)
    }


    fun onShowCategoriaPicker() {
        _state.value = _state.value.copy(showCategoriaPicker = true)
    }


    fun onDismissCategoriaPicker() {
        _state.value = _state.value.copy(showCategoriaPicker = false)
    }


    fun onCategoriaSelecionada(categoriaId: Long) {
        _state.value = _state.value.copy(selectedCategoriaId = categoriaId, showCategoriaPicker = false)
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


    fun onTipoChanged(tipo: TipoARecebimento) {

        if (_state.value.isEdicao) {
            return
        }

        _state.value = _state.value.copy(tipo = tipo)

    }


    fun onTotalParcelasChanged(texto: String) {
        _state.value = _state.value.copy(totalParcelasInput = texto, errorRes = null)
    }


    fun onShowDeleteConfirm() {
        _state.value = _state.value.copy(showDeleteConfirm = true)
    }


    fun onDismissDeleteConfirm() {
        _state.value = _state.value.copy(showDeleteConfirm = false)
    }


    fun onConfirmDelete() {

        val estadoAtual = _state.value

        viewModelScope.launch {

            deleteARecebimentoForward(estadoAtual.editingId)

            _state.value =
                _state.value.copy(showDeleteConfirm = false, showDialog = false)

        }

    }


    fun onSave() {

        val estadoAtual = _state.value

        val valor = parseDecimalInput(estadoAtual.valorInput)
        val categoriaId = estadoAtual.selectedCategoriaId
        val totalParcelas =
            if (estadoAtual.tipo == TipoARecebimento.PARCELADA) {
                estadoAtual.totalParcelasInput.toIntOrNull()
            } else {
                null
            }

        val invalido =
            estadoAtual.descricao.isBlank() ||
                valor == null || valor <= 0.0 ||
                categoriaId == null ||
                estadoAtual.fonteInput.isBlank() ||
                (estadoAtual.tipo == TipoARecebimento.PARCELADA && (totalParcelas == null || totalParcelas <= 0))

        if (invalido) {

            _state.value =
                estadoAtual.copy(errorRes = R.string.arecebimento_invalid_error)

            return

        }

        viewModelScope.launch {

            val fonteId =
                estadoAtual.selectedFonteId
                    ?: addFonteRenda(estadoAtual.fonteInput)

            saveARecebimento(
                arecebimentoId = estadoAtual.editingId,
                tipo = estadoAtual.tipo,
                descricao = estadoAtual.descricao,
                valor = valor,
                dataPrevista = estadoAtual.dataSelecionada,
                categoriaId = categoriaId,
                fonteId = fonteId,
                totalParcelas = totalParcelas
            )

            _state.value = _state.value.copy(showDialog = false)

        }

    }


    fun onMarcarComoPagoClick() {

        val estadoAtual = _state.value
        val hoje = LocalDate.now()

        if (YearMonth.from(hoje) == YearMonth.from(estadoAtual.dataSelecionada)) {

            viewModelScope.launch {
                marcarComoPago(estadoAtual.editingId, hoje)
                _state.value = _state.value.copy(showDialog = false)
            }

        } else {

            _state.value = _state.value.copy(showPagamentoMesDialog = true)

        }

    }


    fun onConfirmarMesAtual() {

        val estadoAtual = _state.value

        viewModelScope.launch {

            marcarComoPago(estadoAtual.editingId, LocalDate.now())

            _state.value =
                _state.value.copy(showPagamentoMesDialog = false, showDialog = false)

        }

    }


    fun onConfirmarMesPrevisto() {

        val estadoAtual = _state.value

        viewModelScope.launch {

            marcarComoPago(estadoAtual.editingId, estadoAtual.dataSelecionada)

            _state.value =
                _state.value.copy(showPagamentoMesDialog = false, showDialog = false)

        }

    }


    fun onCancelarPagamentoMes() {
        _state.value = _state.value.copy(showPagamentoMesDialog = false)
    }

}
