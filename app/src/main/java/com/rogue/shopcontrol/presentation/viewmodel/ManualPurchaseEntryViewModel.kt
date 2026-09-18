package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.domain.model.DivisaoParticipanteInput
import com.rogue.shopcontrol.domain.model.ManualCompraItemInput
import com.rogue.shopcontrol.domain.usecase.AddCategoriaERetornarIdUseCase
import com.rogue.shopcontrol.domain.usecase.AddFonteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.CreateProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.FindProdutoByNameUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllEstabelecimentosUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllFontesRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.SaveFixaOuParceladaUseCase
import com.rogue.shopcontrol.domain.usecase.SaveManualPurchaseUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CampoValorAtivo
import com.rogue.shopcontrol.presentation.viewmodel.states.DivisaoParticipanteDraft
import com.rogue.shopcontrol.presentation.viewmodel.states.ManualPurchaseEntryState
import com.rogue.shopcontrol.presentation.viewmodel.states.ManualPurchaseItemDraft
import com.rogue.shopcontrol.utils.formatDecimalInput
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.parseDecimalInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DATA_COMPRA_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

class ManualPurchaseEntryViewModel(
    private val compraId: Long,
    getCompraById: GetCompraByIdUseCase,
    getAllEstabelecimentos: GetAllEstabelecimentosUseCase,
    getAllProdutos: GetAllProdutosUseCase,
    private val findProdutoByName: FindProdutoByNameUseCase,
    private val createProduto: CreateProdutoUseCase,
    private val saveManualPurchase: SaveManualPurchaseUseCase,
    getCategorias: GetCategoriasUseCase,
    private val saveFixaOuParcelada: SaveFixaOuParceladaUseCase,
    private val addCategoriaERetornarId: AddCategoriaERetornarIdUseCase,
    getAllFontesRenda: GetAllFontesRendaUseCase,
    private val addFonteRenda: AddFonteRendaUseCase
) : ViewModel() {

    private var estabelecimentosCache: List<EstabelecimentoEntity> = emptyList()
    private var produtosCache: List<ProdutoEntity> = emptyList()
    private var fontesCache: List<FonteRendaEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            ManualPurchaseEntryState(isEdicao = compraId != 0L)
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getAllEstabelecimentos().collect { lista ->
                estabelecimentosCache = lista
            }

        }

        viewModelScope.launch {

            getAllProdutos().collect { lista ->
                produtosCache = lista
            }

        }

        viewModelScope.launch {

            getAllFontesRenda().collect { lista ->
                fontesCache = lista
            }

        }

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(
                        categorias = categorias,
                        selectedCategoriaId = _state.value.selectedCategoriaId
                            ?: if (_state.value.tipoCompra != TipoCompra.VARIAVEL) {
                                categorias.firstOrNull()?.id
                            } else {
                                null
                            }
                    )

            }

        }

        if (compraId != 0L) {

            viewModelScope.launch {

                val compra =
                    getCompraById(compraId).first { it != null }

                if (compra != null) {

                    _state.value =
                        _state.value.copy(
                            tipoCompra = compra.compra.tipo,
                            estabelecimentoNome = compra.estabelecimento.nome,
                            estabelecimentoApelido = compra.estabelecimento.apelido ?: "",
                            estabelecimentoExistente = true,
                            dataCompra = parseDataCompra(compra.compra.dataCompra) ?: LocalDate.now(),
                            itens = compra.itens.map { itemCompleto ->
                                ManualPurchaseItemDraft(
                                    produtoId = itemCompleto.produto.id,
                                    produtoNome = itemCompleto.produto.nome,
                                    quantidade = itemCompleto.item.quantidade,
                                    valorUnitario = itemCompleto.item.valorUnitario,
                                    valorTotal = itemCompleto.item.valorTotal
                                )
                            },
                            nomeCompra = compra.compra.nome ?: "",
                            valorCompraInput = if (compra.compra.tipo != TipoCompra.VARIAVEL) {
                                formatDecimalInput(compra.compra.valorTotal)
                            } else {
                                ""
                            },
                            selectedCategoriaId = compra.compra.categoriaId
                                ?: _state.value.selectedCategoriaId,
                            totalParcelasInput = compra.compra.totalParcelas?.toString() ?: ""
                        )

                }

            }

        }

    }


    fun onTipoChanged(tipo: TipoCompra) {

        if (_state.value.isEdicao) {
            return
        }

        _state.value =
            _state.value.copy(
                tipoCompra = tipo,
                selectedCategoriaId = if (tipo != TipoCompra.VARIAVEL && _state.value.selectedCategoriaId == null) {
                    _state.value.categorias.firstOrNull()?.id
                } else {
                    _state.value.selectedCategoriaId
                }
            )

    }


    fun onNomeCompraChanged(texto: String) {

        _state.value =
            _state.value.copy(nomeCompra = texto)

    }


    fun onValorCompraChanged(texto: String) {

        _state.value =
            _state.value.copy(valorCompraInput = texto)

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


    fun onCreateCategoria(nome: String) {

        viewModelScope.launch {

            val categoriaId = addCategoriaERetornarId(nome)

            _state.value =
                _state.value.copy(
                    selectedCategoriaId = categoriaId,
                    showCategoriaPicker = false
                )

        }

    }


    fun onTotalParcelasChanged(texto: String) {

        _state.value =
            _state.value.copy(totalParcelasInput = texto)

    }


    fun onParcelasJaPagasChanged(texto: String) {

        _state.value =
            _state.value.copy(parcelasJaPagasInput = texto)

    }


    fun onEstabelecimentoNomeChanged(texto: String) {

        val nomeTrim = texto.trim()

        val existente =
            estabelecimentosCache.firstOrNull {
                it.nome.equals(nomeTrim, ignoreCase = true)
            }

        _state.value =
            _state.value.copy(
                estabelecimentoNome = texto,
                estabelecimentoApelido = existente?.apelido ?: _state.value.estabelecimentoApelido,
                estabelecimentoExistente = existente != null,
                selectedCategoriaId = if (existente != null && _state.value.tipoCompra == TipoCompra.VARIAVEL) {
                    existente.categoriaId
                } else {
                    _state.value.selectedCategoriaId
                },
                estabelecimentosSugeridos = if (texto.isBlank()) {
                    emptyList()
                } else {
                    estabelecimentosCache.filter { estabelecimento ->
                        estabelecimento.nome.contains(texto, ignoreCase = true) ||
                            estabelecimento.apelido?.contains(texto, ignoreCase = true) == true
                    }.take(5)
                }
            )

    }


    fun onEstabelecimentoSugestaoSelecionada(estabelecimento: EstabelecimentoEntity) {

        _state.value =
            _state.value.copy(
                estabelecimentoNome = estabelecimento.nome,
                estabelecimentoApelido = estabelecimento.apelido ?: "",
                estabelecimentoExistente = true,
                estabelecimentosSugeridos = emptyList(),
                selectedCategoriaId = if (_state.value.tipoCompra == TipoCompra.VARIAVEL) {
                    estabelecimento.categoriaId
                } else {
                    _state.value.selectedCategoriaId
                }
            )

    }


    fun onEstabelecimentoApelidoChanged(texto: String) {

        _state.value =
            _state.value.copy(
                estabelecimentoApelido = texto
            )

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
            _state.value.copy(
                dataCompra = data,
                showDatePicker = false
            )

    }


    fun onShowAddItemDialog() {

        _state.value =
            _state.value.copy(
                showAddItemDialog = true,
                itemNomeInput = "",
                produtosSugeridos = emptyList(),
                itemQuantidade = "",
                itemValorUnitario = "",
                itemValorTotal = "",
                campoValorAtivo = CampoValorAtivo.UNITARIO,
                itemErrorRes = null
            )

    }


    fun onDismissAddItemDialog() {

        _state.value =
            _state.value.copy(showAddItemDialog = false)

    }


    fun onItemNomeChanged(texto: String) {

        _state.value =
            _state.value.copy(
                itemNomeInput = texto,
                produtosSugeridos = if (texto.isBlank()) {
                    emptyList()
                } else {
                    produtosCache.filter {
                        it.nome.contains(texto, ignoreCase = true)
                    }.take(5)
                },
                itemErrorRes = null
            )

    }


    fun onItemSugestaoSelecionada(produto: ProdutoEntity) {

        _state.value =
            _state.value.copy(
                itemNomeInput = produto.nome,
                produtosSugeridos = emptyList()
            )

    }


    fun onItemQuantidadeChanged(texto: String) {

        val estadoAtual = _state.value
        val qtd = parseDecimalInput(texto)

        _state.value =
            when (estadoAtual.campoValorAtivo) {

                CampoValorAtivo.UNITARIO -> {

                    val unit = parseDecimalInput(estadoAtual.itemValorUnitario)
                    val total =
                        if (qtd != null && unit != null) {
                            formatDecimalInput(qtd * unit)
                        } else {
                            estadoAtual.itemValorTotal
                        }

                    estadoAtual.copy(
                        itemQuantidade = texto,
                        itemValorTotal = total,
                        itemErrorRes = null
                    )

                }

                CampoValorAtivo.TOTAL -> {

                    val total = parseDecimalInput(estadoAtual.itemValorTotal)
                    val unit =
                        if (qtd != null && qtd != 0.0 && total != null) {
                            formatDecimalInput(total / qtd)
                        } else {
                            estadoAtual.itemValorUnitario
                        }

                    estadoAtual.copy(
                        itemQuantidade = texto,
                        itemValorUnitario = unit,
                        itemErrorRes = null
                    )

                }

            }

    }


    fun onItemValorUnitarioChanged(texto: String) {

        val estadoAtual = _state.value
        val qtd = parseDecimalInput(estadoAtual.itemQuantidade)
        val unit = parseDecimalInput(texto)

        val total =
            if (qtd != null && unit != null) {
                formatDecimalInput(qtd * unit)
            } else {
                estadoAtual.itemValorTotal
            }

        _state.value =
            estadoAtual.copy(
                itemValorUnitario = texto,
                itemValorTotal = total,
                campoValorAtivo = CampoValorAtivo.UNITARIO,
                itemErrorRes = null
            )

    }


    fun onItemValorTotalChanged(texto: String) {

        val estadoAtual = _state.value
        val qtd = parseDecimalInput(estadoAtual.itemQuantidade)
        val total = parseDecimalInput(texto)

        val unit =
            if (qtd != null && qtd != 0.0 && total != null) {
                formatDecimalInput(total / qtd)
            } else {
                estadoAtual.itemValorUnitario
            }

        _state.value =
            estadoAtual.copy(
                itemValorTotal = texto,
                itemValorUnitario = unit,
                campoValorAtivo = CampoValorAtivo.TOTAL,
                itemErrorRes = null
            )

    }


    fun onConfirmAddItem() {

        viewModelScope.launch {

            val estadoAtual = _state.value
            val nome = estadoAtual.itemNomeInput.trim()
            val qtd = parseDecimalInput(estadoAtual.itemQuantidade)
            val unit = parseDecimalInput(estadoAtual.itemValorUnitario)
            val total = parseDecimalInput(estadoAtual.itemValorTotal)

            if (nome.isBlank() || qtd == null || qtd <= 0.0 || unit == null || total == null || total <= 0.0) {

                _state.value =
                    estadoAtual.copy(itemErrorRes = R.string.manual_item_invalid_error)

                return@launch

            }

            val produtoExistente = findProdutoByName(nome)

            if (produtoExistente == null) {

                _state.value =
                    estadoAtual.copy(
                        showConfirmNewProduct = true,
                        pendingNovoProdutoNome = nome
                    )

                return@launch

            }

            adicionarItemAoRascunho(produtoExistente.id, produtoExistente.nome, qtd, unit, total)

        }

    }


    fun onConfirmNewProduct() {

        viewModelScope.launch {

            val estadoAtual = _state.value
            val qtd = parseDecimalInput(estadoAtual.itemQuantidade) ?: return@launch
            val unit = parseDecimalInput(estadoAtual.itemValorUnitario) ?: return@launch
            val total = parseDecimalInput(estadoAtual.itemValorTotal) ?: return@launch

            val produtoId = createProduto(estadoAtual.pendingNovoProdutoNome)

            adicionarItemAoRascunho(produtoId, estadoAtual.pendingNovoProdutoNome, qtd, unit, total)

        }

    }


    fun onDismissConfirmNewProduct() {

        _state.value =
            _state.value.copy(
                showConfirmNewProduct = false,
                pendingNovoProdutoNome = ""
            )

    }


    private fun adicionarItemAoRascunho(
        produtoId: Long,
        produtoNome: String,
        quantidade: Double,
        valorUnitario: Double,
        valorTotal: Double
    ) {

        val estadoAtual = _state.value

        val jaExiste =
            estadoAtual.itens.any { it.produtoId == produtoId }

        if (jaExiste) {

            _state.value =
                estadoAtual.copy(
                    showConfirmNewProduct = false,
                    itemErrorRes = R.string.manual_item_duplicate_error
                )

            return

        }

        _state.value =
            estadoAtual.copy(
                itens = estadoAtual.itens + ManualPurchaseItemDraft(
                    produtoId = produtoId,
                    produtoNome = produtoNome,
                    quantidade = quantidade,
                    valorUnitario = valorUnitario,
                    valorTotal = valorTotal
                ),
                showAddItemDialog = false,
                showConfirmNewProduct = false
            )

    }


    fun onRemoveItem(produtoId: Long) {

        _state.value =
            _state.value.copy(
                itens = _state.value.itens.filterNot { it.produtoId == produtoId }
            )

    }


    fun onDividirContaChanged(valor: Boolean) {

        _state.value =
            _state.value.copy(
                dividirConta = valor,
                divisaoErrorRes = null
            )

    }


    fun onDividirIgualmenteChanged(valor: Boolean) {

        _state.value =
            _state.value.copy(dividirIgualmente = valor)

        if (valor) {
            recalcularDivisaoIgualitaria()
        }

    }


    fun onParticipanteInputChanged(texto: String) {

        _state.value =
            _state.value.copy(
                participanteInput = texto,
                participantesSugeridos = if (texto.isBlank()) {
                    emptyList()
                } else {
                    fontesCache.filter {
                        it.nome.contains(texto, ignoreCase = true) &&
                            _state.value.participantes.none { p -> p.fonteId == it.id }
                    }.take(5)
                },
                divisaoErrorRes = null
            )

    }


    fun onParticipanteSugestaoSelecionada(fonte: FonteRendaEntity) {

        adicionarParticipante(fonte.id, fonte.nome)

    }


    fun onConfirmAddParticipante() {

        viewModelScope.launch {

            val nome = _state.value.participanteInput.trim()

            if (nome.isBlank()) {
                return@launch
            }

            val existente =
                fontesCache.firstOrNull { it.nome.equals(nome, ignoreCase = true) }

            if (existente != null) {

                if (_state.value.participantes.any { it.fonteId == existente.id }) {

                    _state.value =
                        _state.value.copy(divisaoErrorRes = R.string.divisao_participante_duplicado_error)

                    return@launch

                }

                adicionarParticipante(existente.id, existente.nome)

            } else {

                val fonteId = addFonteRenda(nome)

                adicionarParticipante(fonteId, nome)

            }

        }

    }


    private fun adicionarParticipante(fonteId: Long, nome: String) {

        _state.value =
            _state.value.copy(
                participantes = _state.value.participantes + DivisaoParticipanteDraft(
                    fonteId = fonteId,
                    nome = nome,
                    valorInput = formatDecimalInput(0.0)
                ),
                participanteInput = "",
                participantesSugeridos = emptyList()
            )

        if (_state.value.dividirIgualmente) {
            recalcularDivisaoIgualitaria()
        }

    }


    fun onRemoveParticipante(fonteId: Long) {

        _state.value =
            _state.value.copy(
                participantes = _state.value.participantes.filterNot { it.fonteId == fonteId }
            )

        if (_state.value.dividirIgualmente) {
            recalcularDivisaoIgualitaria()
        }

    }


    fun onParticipanteValorChanged(fonteId: Long, texto: String) {

        _state.value =
            _state.value.copy(
                participantes = _state.value.participantes.map { participante ->
                    if (participante.fonteId == fonteId) {
                        participante.copy(valorInput = texto)
                    } else {
                        participante
                    }
                }
            )

    }


    private fun recalcularDivisaoIgualitaria() {

        val estadoAtual = _state.value
        val quantidadePessoas = estadoAtual.participantes.size + 1

        if (quantidadePessoas <= 1) {
            return
        }

        val valorPorPessoa = estadoAtual.valorTotalPago / quantidadePessoas

        _state.value =
            estadoAtual.copy(
                participantes = estadoAtual.participantes.map { it.copy(valorInput = formatDecimalInput(valorPorPessoa)) }
            )

    }


    fun onSave() {

        viewModelScope.launch {

            val estadoAtual = _state.value

            if (!estadoAtual.podeSalvar) {
                return@launch
            }

            _state.value = estadoAtual.copy(isSaving = true)

            val participantesInput =
                if (estadoAtual.dividirConta) {
                    estadoAtual.participantes.map { DivisaoParticipanteInput(it.fonteId, it.valor) }
                } else {
                    emptyList()
                }

            val idSalvo =
                if (estadoAtual.tipoCompra == TipoCompra.VARIAVEL) {

                    val dataFormatada =
                        LocalDateTime.of(estadoAtual.dataCompra, LocalDateTime.now().toLocalTime())
                            .format(DATA_COMPRA_FORMATTER)

                    saveManualPurchase(
                        compraId = compraId,
                        estabelecimentoNome = estadoAtual.estabelecimentoNome,
                        estabelecimentoApelido = estadoAtual.estabelecimentoApelido.trim().ifBlank { null },
                        dataCompra = dataFormatada,
                        categoriaId = estadoAtual.selectedCategoriaId,
                        itens = estadoAtual.itens.map { item ->
                            ManualCompraItemInput(
                                produtoId = item.produtoId,
                                quantidade = item.quantidade,
                                valorUnitario = item.valorUnitario,
                                valorTotal = item.valorTotal
                            )
                        },
                        participantes = participantesInput
                    )

                } else {

                    val valor = parseDecimalInput(estadoAtual.valorCompraInput) ?: 0.0
                    val totalParcelas = estadoAtual.totalParcelasInput.toIntOrNull()
                    val parcelasJaPagas = estadoAtual.parcelasJaPagasInput.toIntOrNull() ?: 0

                    saveFixaOuParcelada(
                        compraId = compraId,
                        tipo = estadoAtual.tipoCompra,
                        nome = estadoAtual.nomeCompra,
                        valor = valor,
                        estabelecimentoNome = estadoAtual.estabelecimentoNome,
                        estabelecimentoApelido = estadoAtual.estabelecimentoApelido.trim().ifBlank { null },
                        data = estadoAtual.dataCompra,
                        categoriaId = estadoAtual.selectedCategoriaId ?: 0L,
                        totalParcelas = totalParcelas,
                        parcelasJaPagas = parcelasJaPagas,
                        participantes = participantesInput
                    )

                }

            _state.value =
                _state.value.copy(
                    isSaving = false,
                    savedCompraId = idSalvo
                )

        }

    }

}
