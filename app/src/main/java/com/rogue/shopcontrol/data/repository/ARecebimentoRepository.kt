package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.ARecebimentoDao
import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.local.entity.ARecebimentoEntity
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import com.rogue.shopcontrol.utils.formatDataCompra
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.plusMonthsClamped
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

private const val LOTE_TAMANHO = 12
private const val DIA_FIXO = 10
private const val LIMIAR_EXTENSAO_MESES = 2L

class ARecebimentoRepository(
    private val arecebimentoDao: ARecebimentoDao,
    private val rendaRepository: RendaRepository
) {

    fun getARecebimentosComDados(): Flow<List<ARecebimentoComDados>> =
        arecebimentoDao.getARecebimentosComDados()


    suspend fun saveARecebimento(
        arecebimentoId: Long,
        tipo: TipoARecebimento,
        descricao: String,
        valor: Double,
        dataPrevista: LocalDate,
        categoriaId: Long,
        fonteId: Long,
        totalParcelas: Int?
    ): Long {

        val descricao = capitalizarPrimeiraLetra(descricao)

        if (arecebimentoId != 0L) {

            collectForwardChain(arecebimentoId).forEach { item ->

                arecebimentoDao.updateCampos(
                    id = item.id,
                    descricao = descricao,
                    valor = valor,
                    categoriaId = categoriaId,
                    fonteId = fonteId
                )

            }

            return arecebimentoId

        }

        return when (tipo) {

            TipoARecebimento.PONTUAL ->

                arecebimentoDao.insert(
                    ARecebimentoEntity(
                        descricao = descricao,
                        valor = valor,
                        dataPrevista = formatDataCompra(dataPrevista),
                        rendaCategoriaId = categoriaId,
                        fonteRendaId = fonteId,
                        tipo = TipoARecebimento.PONTUAL
                    )
                )

            TipoARecebimento.FIXA -> {

                val rootId =
                    arecebimentoDao.insert(
                        ARecebimentoEntity(
                            descricao = descricao,
                            valor = valor,
                            dataPrevista = formatDataCompra(dataPrevista),
                            rendaCategoriaId = categoriaId,
                            fonteRendaId = fonteId,
                            tipo = TipoARecebimento.FIXA
                        )
                    )

                gerarLoteFixa(
                    descricao = descricao,
                    valor = valor,
                    categoriaId = categoriaId,
                    fonteId = fonteId,
                    mesBase = YearMonth.from(dataPrevista),
                    idAnterior = rootId
                )

                rootId

            }

            TipoARecebimento.PARCELADA ->

                criarSerieParcelada(
                    descricao = descricao,
                    valor = valor,
                    categoriaId = categoriaId,
                    fonteId = fonteId,
                    dataPrevista = dataPrevista,
                    totalParcelas = totalParcelas ?: 1
                )

        }

    }


    suspend fun deleteARecebimentoForward(
        arecebimentoId: Long
    ) {

        val alvo =
            arecebimentoDao.findByIdOnce(arecebimentoId) ?: return

        val cadeia =
            collectForwardChain(arecebimentoId)

        if (alvo.tipo == TipoARecebimento.FIXA) {

            alvo.origemRecorrenteId?.let { parentId ->
                arecebimentoDao.setAtivo(parentId, false)
            }

        }

        cadeia
            .filter { it.pago && it.rendaGeradaId != null }
            .forEach { item ->
                rendaRepository.deleteRendaById(item.rendaGeradaId!!)
            }

        arecebimentoDao.deleteByIds(cadeia.map { it.id })

    }


    suspend fun marcarComoPago(
        arecebimentoId: Long,
        dataRendaEscolhida: LocalDate
    ) {

        val alvo =
            arecebimentoDao.findByIdOnce(arecebimentoId) ?: return

        val rendaId =
            rendaRepository.criarRendaAvulsa(
                descricao = alvo.descricao,
                valor = alvo.valor,
                data = dataRendaEscolhida,
                categoriaId = alvo.rendaCategoriaId,
                fonteId = alvo.fonteRendaId
            )

        arecebimentoDao.marcarComoPago(
            id = arecebimentoId,
            dataPagamento = formatDataCompra(LocalDate.now()),
            rendaGeradaId = rendaId
        )

    }


    suspend fun verificarEEstenderSeries() {

        val tails =
            arecebimentoDao.findFixaTailsAtivas()

        tails.forEach { tail ->

            val dataTail =
                parseDataCompra(tail.dataPrevista) ?: return@forEach

            if (dataTail.isBefore(LocalDate.now().plusMonths(LIMIAR_EXTENSAO_MESES))) {

                gerarLoteFixa(
                    descricao = tail.descricao,
                    valor = tail.valor,
                    categoriaId = tail.rendaCategoriaId,
                    fonteId = tail.fonteRendaId,
                    mesBase = YearMonth.from(dataTail),
                    idAnterior = tail.id
                )

            }

        }

    }


    private suspend fun collectForwardChain(
        startId: Long
    ): List<ARecebimentoEntity> {

        val primeiro =
            arecebimentoDao.findByIdOnce(startId) ?: return emptyList()

        val resultado = mutableListOf(primeiro)
        var idAtual = startId

        while (true) {

            val filho =
                arecebimentoDao.findByOrigemRecorrenteId(idAtual) ?: break

            resultado.add(filho)
            idAtual = filho.id

        }

        return resultado

    }


    private suspend fun gerarLoteFixa(
        descricao: String,
        valor: Double,
        categoriaId: Long,
        fonteId: Long,
        mesBase: YearMonth,
        idAnterior: Long
    ) {

        var mesAtual = mesBase.plusMonths(1)
        var ultimoId = idAnterior

        repeat(LOTE_TAMANHO) {

            val novaData = mesAtual.atDay(DIA_FIXO)

            ultimoId =
                arecebimentoDao.insert(
                    ARecebimentoEntity(
                        descricao = descricao,
                        valor = valor,
                        dataPrevista = formatDataCompra(novaData),
                        rendaCategoriaId = categoriaId,
                        fonteRendaId = fonteId,
                        tipo = TipoARecebimento.FIXA,
                        origemRecorrenteId = ultimoId
                    )
                )

            mesAtual = mesAtual.plusMonths(1)

        }

    }


    private suspend fun criarSerieParcelada(
        descricao: String,
        valor: Double,
        categoriaId: Long,
        fonteId: Long,
        dataPrevista: LocalDate,
        totalParcelas: Int
    ): Long {

        var idAnterior: Long? = null
        var idPrimeira = 0L

        for (numero in 1..totalParcelas) {

            val dataParcela =
                dataPrevista.plusMonthsClamped((numero - 1).toLong())

            val novoId =
                arecebimentoDao.insert(
                    ARecebimentoEntity(
                        descricao = descricao,
                        valor = valor,
                        dataPrevista = formatDataCompra(dataParcela),
                        rendaCategoriaId = categoriaId,
                        fonteRendaId = fonteId,
                        tipo = TipoARecebimento.PARCELADA,
                        totalParcelas = totalParcelas,
                        origemRecorrenteId = idAnterior
                    )
                )

            if (numero == 1) {
                idPrimeira = novoId
            }

            idAnterior = novoId

        }

        return idPrimeira

    }

}
