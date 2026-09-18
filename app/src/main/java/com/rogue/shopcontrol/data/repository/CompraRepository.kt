package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.ARecebimentoDao
import com.rogue.shopcontrol.data.local.dao.CategoriaDao
import com.rogue.shopcontrol.data.local.dao.CompraDao
import com.rogue.shopcontrol.data.local.dao.DivisaoContaDao
import com.rogue.shopcontrol.data.local.dao.EstabelecimentoDao
import com.rogue.shopcontrol.data.local.dao.ProdutoDao
import com.rogue.shopcontrol.data.local.entity.ARecebimentoEntity
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.DivisaoContaEntity
import com.rogue.shopcontrol.data.local.entity.DivisaoParticipanteComNome
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
import com.rogue.shopcontrol.data.local.entity.ItemPrecoHistorico
import com.rogue.shopcontrol.data.local.entity.CompraComData
import com.rogue.shopcontrol.data.local.entity.OrigemCompra
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.domain.model.DivisaoParticipanteInput
import com.rogue.shopcontrol.domain.model.ManualCompraItemInput
import com.rogue.shopcontrol.domain.model.NfceData
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import com.rogue.shopcontrol.utils.formatDataCompra
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

private const val LOTE_TAMANHO = 12
private const val DIA_FIXO = 10
private const val LIMIAR_EXTENSAO_MESES = 2L
private const val SEM_CATEGORIA_NOME = "Sem categoria"

class CompraRepository(
    private val estabelecimentoDao: EstabelecimentoDao,
    private val produtoDao: ProdutoDao,
    private val compraDao: CompraDao,
    private val categoriaDao: CategoriaDao,
    private val arecebimentoDao: ARecebimentoDao,
    private val divisaoContaDao: DivisaoContaDao,
    private val rendaCategoriaRepository: RendaCategoriaRepository
) {


    suspend fun save(
        data: NfceData
    ): Long {


        val compraExistenteId =
            compraDao.findCompraId(
                cnpjEstabelecimento = data.estabelecimento.cnpj,
                dataCompra = data.dataEmissao
            )

        if (compraExistenteId != null) {
            return compraExistenteId
        }


        val estabelecimentoExistente =
            estabelecimentoDao.findByCnpj(
                data.estabelecimento.cnpj
            )

        val estabelecimentoId =
            estabelecimentoExistente
                ?.id
                ?: estabelecimentoDao.insert(
                    EstabelecimentoEntity(
                        nome = data.estabelecimento.nome,
                        cnpj = data.estabelecimento.cnpj,
                        endereco = data.estabelecimento.endereco
                    )
                )


        val compraId =
            compraDao.insert(
                CompraEntity(
                    estabelecimentoId = estabelecimentoId,
                    chaveNfce = data.chaveAcesso,
                    dataCompra = data.dataEmissao,
                    valorTotal = data.valorTotal,
                    categoriaId = estabelecimentoExistente?.categoriaId
                )
            )


        val itens = mutableListOf<ItemCompraEntity>()


        data.produtos.forEach { produto ->


            val produtoId =
                produtoDao.findByName(
                    produto.nome
                )
                    ?.id
                    ?: produtoDao.insert(
                        ProdutoEntity(
                            nome = produto.nome,
                            codigo = produto.codigo
                        )
                    )


            itens.add(
                ItemCompraEntity(
                    compraId = compraId,
                    produtoId = produtoId,
                    quantidade = produto.quantidade,
                    valorUnitario = produto.valorUnitario,
                    valorTotal = produto.valorTotal
                )
            )

        }


        compraDao.insertItens(itens)

        return compraId

    }


    suspend fun saveManual(
        compraId: Long,
        estabelecimentoNome: String,
        estabelecimentoApelido: String?,
        dataCompra: String?,
        categoriaId: Long?,
        itens: List<ManualCompraItemInput>,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ): Long {

        val nomeTrim = capitalizarPrimeiraLetra(estabelecimentoNome)

        val estabelecimentoId =
            estabelecimentoDao.findByNome(nomeTrim)
                ?.id
                ?: estabelecimentoDao.insert(
                    EstabelecimentoEntity(
                        nome = nomeTrim,
                        cnpj = "",
                        endereco = "",
                        apelido = estabelecimentoApelido?.let { capitalizarPrimeiraLetra(it) }
                    )
                )

        val valorPago = itens.sumOf { it.valorTotal }
        val suaParte = valorPago - participantes.sumOf { it.valor }

        val idFinal =
            if (compraId == 0L) {

                compraDao.insert(
                    CompraEntity(
                        estabelecimentoId = estabelecimentoId,
                        dataCompra = dataCompra,
                        valorTotal = suaParte,
                        origem = OrigemCompra.MANUAL,
                        categoriaId = categoriaId,
                        dividida = participantes.isNotEmpty()
                    )
                )

            } else {

                val participantesExistentes =
                    divisaoContaDao.getByCompraIdOnce(compraId)

                val valorParaArmazenar =
                    valorPago - participantesExistentes.sumOf { it.valor }

                compraDao.updateManualInfo(
                    compraId = compraId,
                    dataCompra = dataCompra,
                    estabelecimentoId = estabelecimentoId,
                    valorTotal = valorParaArmazenar,
                    categoriaId = categoriaId
                )

                compraDao.deleteItensByCompraId(compraId)

                compraId

            }

        compraDao.insertItens(
            itens.map { item ->
                ItemCompraEntity(
                    compraId = idFinal,
                    produtoId = item.produtoId,
                    quantidade = item.quantidade,
                    valorUnitario = item.valorUnitario,
                    valorTotal = item.valorTotal
                )
            }
        )

        if (compraId == 0L && participantes.isNotEmpty()) {

            criarDivisaoConta(
                compraId = idFinal,
                categoriaId = categoriaId,
                nomeContexto = nomeTrim,
                data = dataCompra,
                participantes = participantes
            )

        }

        return idFinal

    }


    fun getTodosPrecos(): Flow<List<ItemPrecoHistorico>> =
        compraDao.getTodosPrecos()


    fun getCompras(): Flow<List<CompraCompleta>> =
        compraDao.getCompras()


    fun getCompraById(
        compraId: Long
    ): Flow<CompraCompleta?> =
        compraDao.getCompraById(compraId)


    fun getComprasByEstabelecimento(
        estabelecimentoId: Long
    ): Flow<List<CompraCompleta>> =
        compraDao.getComprasByEstabelecimento(estabelecimentoId)


    fun getDivisaoConta(
        compraId: Long
    ): Flow<List<DivisaoParticipanteComNome>> =
        divisaoContaDao.getByCompraId(compraId)


    suspend fun deleteCompra(
        compraId: Long
    ) {

        deletarArecebimentosPendentesDaCompra(listOf(compraId))

        compraDao.deleteCompraCompleta(compraId)

    }


    fun getComprasComData(): Flow<List<CompraComData>> =
        compraDao.getComprasComData()


    suspend fun saveFixaOuParcelada(
        compraId: Long,
        tipo: TipoCompra,
        nome: String,
        valor: Double,
        estabelecimentoNome: String,
        estabelecimentoApelido: String?,
        data: LocalDate,
        categoriaId: Long,
        totalParcelas: Int?,
        parcelasJaPagas: Int,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ): Long {

        val nomeCapitalizado = capitalizarPrimeiraLetra(nome)
        val nomeEstabelecimentoTrim = capitalizarPrimeiraLetra(estabelecimentoNome)

        val estabelecimentoId =
            estabelecimentoDao.findByNome(nomeEstabelecimentoTrim)
                ?.id
                ?: estabelecimentoDao.insert(
                    EstabelecimentoEntity(
                        nome = nomeEstabelecimentoTrim,
                        cnpj = "",
                        endereco = "",
                        apelido = estabelecimentoApelido?.let { capitalizarPrimeiraLetra(it) }
                    )
                )

        if (compraId != 0L) {

            collectForwardChainCompra(compraId).forEach { id ->

                compraDao.updateCamposFixaParcelada(
                    id = id,
                    nome = nomeCapitalizado,
                    valorTotal = valor,
                    categoriaId = categoriaId,
                    estabelecimentoId = estabelecimentoId
                )

            }

            return compraId

        }

        val suaParte = valor - participantes.sumOf { it.valor }
        val dividida = participantes.isNotEmpty()

        return when (tipo) {

            TipoCompra.FIXA -> {

                val dataFormatada = formatDataCompra(data)

                val rootId =
                    compraDao.insert(
                        CompraEntity(
                            estabelecimentoId = estabelecimentoId,
                            dataCompra = dataFormatada,
                            valorTotal = suaParte,
                            origem = OrigemCompra.MANUAL,
                            tipo = TipoCompra.FIXA,
                            nome = nomeCapitalizado,
                            categoriaId = categoriaId,
                            dataCompetencia = dataFormatada,
                            dividida = dividida
                        )
                    )

                if (dividida) {

                    criarDivisaoConta(
                        compraId = rootId,
                        categoriaId = categoriaId,
                        nomeContexto = nomeCapitalizado,
                        data = dataFormatada,
                        participantes = participantes
                    )

                }

                gerarLoteFixa(
                    nome = nomeCapitalizado,
                    valorParaArmazenar = suaParte,
                    categoriaId = categoriaId,
                    estabelecimentoId = estabelecimentoId,
                    mesBase = YearMonth.from(data),
                    idAnterior = rootId,
                    dividida = dividida,
                    participantes = participantes
                )

                rootId

            }

            TipoCompra.RAPIDA -> {

                val dataFormatada = formatDataCompra(data)

                val novoId =
                    compraDao.insert(
                        CompraEntity(
                            estabelecimentoId = estabelecimentoId,
                            dataCompra = dataFormatada,
                            valorTotal = suaParte,
                            origem = OrigemCompra.MANUAL,
                            tipo = TipoCompra.RAPIDA,
                            nome = nomeCapitalizado,
                            categoriaId = categoriaId,
                            dataCompetencia = dataFormatada,
                            dividida = dividida
                        )
                    )

                if (dividida) {

                    criarDivisaoConta(
                        compraId = novoId,
                        categoriaId = categoriaId,
                        nomeContexto = nomeCapitalizado,
                        data = dataFormatada,
                        participantes = participantes
                    )

                }

                novoId

            }

            else -> {

                criarSerieParcelada(
                    nome = nomeCapitalizado,
                    valorParaArmazenar = suaParte,
                    categoriaId = categoriaId,
                    estabelecimentoId = estabelecimentoId,
                    data = data,
                    totalParcelas = totalParcelas ?: 1,
                    parcelasJaPagas = parcelasJaPagas,
                    dividida = dividida,
                    participantes = participantes
                )

            }

        }

    }


    suspend fun deleteCompraForward(
        compraId: Long
    ) {

        val alvo =
            compraDao.findByIdOnce(compraId) ?: return

        val cadeia =
            collectForwardChainCompra(compraId)

        if (alvo.tipo == TipoCompra.FIXA) {

            alvo.origemRecorrenteId?.let { parentId ->
                compraDao.setAtivo(parentId, false)
            }

        }

        deletarArecebimentosPendentesDaCompra(cadeia)

        compraDao.deleteByIds(cadeia)

    }


    suspend fun verificarEEstenderSeriesFixa() {

        val tails =
            compraDao.findFixaTailsAtivas()

        tails.forEach { tail ->

            val categoriaId = tail.categoriaId ?: return@forEach
            val nome = tail.nome ?: return@forEach
            val dataTail = parseDataCompra(tail.dataCompra) ?: return@forEach

            if (dataTail.isBefore(LocalDate.now().plusMonths(LIMIAR_EXTENSAO_MESES))) {

                val participantesTemplate =
                    if (tail.dividida) {
                        divisaoContaDao.getByCompraIdOnce(tail.id).map {
                            DivisaoParticipanteInput(it.fonteRendaId, it.valor)
                        }
                    } else {
                        emptyList()
                    }

                gerarLoteFixa(
                    nome = nome,
                    valorParaArmazenar = tail.valorTotal,
                    categoriaId = categoriaId,
                    estabelecimentoId = tail.estabelecimentoId,
                    mesBase = YearMonth.from(dataTail),
                    idAnterior = tail.id,
                    dividida = tail.dividida,
                    participantes = participantesTemplate
                )

            }

        }

    }


    suspend fun getParcelasDaSerie(
        compraId: Long
    ): List<CompraEntity> {

        var rootId = compraId
        var atual = compraDao.findByIdOnce(compraId)

        while (atual?.origemRecorrenteId != null) {

            rootId = atual.origemRecorrenteId
            atual = compraDao.findByIdOnce(rootId)

        }

        return collectForwardChainCompra(rootId).mapNotNull { compraDao.findByIdOnce(it) }

    }


    private suspend fun deletarArecebimentosPendentesDaCompra(
        compraIds: List<Long>
    ) {

        val ligados =
            arecebimentoDao.findByCompraOrigemIds(compraIds)

        val pendentes =
            ligados.filter { !it.pago }

        arecebimentoDao.deleteByIds(pendentes.map { it.id })

        divisaoContaDao.deleteByCompraIds(compraIds)

    }


    private suspend fun criarDivisaoConta(
        compraId: Long,
        categoriaId: Long?,
        nomeContexto: String,
        data: String?,
        participantes: List<DivisaoParticipanteInput>
    ) {

        val categoriaNome =
            categoriaId?.let { categoriaDao.findById(it)?.nome } ?: SEM_CATEGORIA_NOME

        val rendaCategoriaId =
            rendaCategoriaRepository.addCategoriaERetornarId(categoriaNome)

        val dataFormatadaCurta = data?.take(10)
        val descricao =
            "Sua parte em $nomeContexto" + (dataFormatadaCurta?.let { ", $it" } ?: "")

        participantes.forEach { participante ->

            divisaoContaDao.insert(
                DivisaoContaEntity(
                    compraId = compraId,
                    fonteRendaId = participante.fonteRendaId,
                    valor = participante.valor
                )
            )

            arecebimentoDao.insert(
                ARecebimentoEntity(
                    descricao = descricao,
                    valor = participante.valor,
                    dataPrevista = data ?: formatDataCompra(LocalDate.now()),
                    rendaCategoriaId = rendaCategoriaId,
                    fonteRendaId = participante.fonteRendaId,
                    tipo = TipoARecebimento.PONTUAL,
                    compraOrigemId = compraId
                )
            )

        }

    }


    private suspend fun collectForwardChainCompra(
        startId: Long
    ): List<Long> {

        val resultado = mutableListOf(startId)
        var idAtual = startId

        while (true) {

            val filho =
                compraDao.findByOrigemRecorrenteId(idAtual) ?: break

            resultado.add(filho.id)
            idAtual = filho.id

        }

        return resultado

    }


    private suspend fun gerarLoteFixa(
        nome: String,
        valorParaArmazenar: Double,
        categoriaId: Long,
        estabelecimentoId: Long,
        mesBase: YearMonth,
        idAnterior: Long,
        dividida: Boolean = false,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ) {

        var mesAtual = mesBase.plusMonths(1)
        var ultimoId = idAnterior

        repeat(LOTE_TAMANHO) {

            val novaData = mesAtual.atDay(DIA_FIXO)
            val dataFormatada = formatDataCompra(novaData)

            ultimoId =
                compraDao.insert(
                    CompraEntity(
                        estabelecimentoId = estabelecimentoId,
                        dataCompra = dataFormatada,
                        valorTotal = valorParaArmazenar,
                        origem = OrigemCompra.MANUAL,
                        tipo = TipoCompra.FIXA,
                        nome = nome,
                        categoriaId = categoriaId,
                        origemRecorrenteId = ultimoId,
                        dataCompetencia = dataFormatada,
                        dividida = dividida
                    )
                )

            if (dividida) {

                criarDivisaoConta(
                    compraId = ultimoId,
                    categoriaId = categoriaId,
                    nomeContexto = nome,
                    data = dataFormatada,
                    participantes = participantes
                )

            }

            mesAtual = mesAtual.plusMonths(1)

        }

    }


    private suspend fun criarSerieParcelada(
        nome: String,
        valorParaArmazenar: Double,
        categoriaId: Long,
        estabelecimentoId: Long,
        data: LocalDate,
        totalParcelas: Int,
        parcelasJaPagas: Int,
        dividida: Boolean = false,
        participantes: List<DivisaoParticipanteInput> = emptyList()
    ): Long {

        val dataFormatada = formatDataCompra(data)
        val mesBase = YearMonth.from(data)

        var idAnterior: Long? = null
        var idParcelaAtual = 0L

        for (numero in 1..totalParcelas) {

            val offsetMeses = (numero - (parcelasJaPagas + 1)).toLong()

            val dataCompetencia =
                formatDataCompra(mesBase.plusMonths(offsetMeses).atDay(DIA_FIXO))

            val novoId =
                compraDao.insert(
                    CompraEntity(
                        estabelecimentoId = estabelecimentoId,
                        dataCompra = dataFormatada,
                        valorTotal = valorParaArmazenar,
                        origem = OrigemCompra.MANUAL,
                        tipo = TipoCompra.PARCELADA,
                        nome = nome,
                        categoriaId = categoriaId,
                        origemRecorrenteId = idAnterior,
                        totalParcelas = totalParcelas,
                        dataCompetencia = dataCompetencia,
                        dividida = dividida
                    )
                )

            if (dividida) {

                criarDivisaoConta(
                    compraId = novoId,
                    categoriaId = categoriaId,
                    nomeContexto = nome,
                    data = dataCompetencia,
                    participantes = participantes
                )

            }

            if (numero == parcelasJaPagas + 1) {
                idParcelaAtual = novoId
            }

            idAnterior = novoId

        }

        return idParcelaAtual

    }

}
