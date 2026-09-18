package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.RendaDao
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.data.local.entity.RendaEntity
import com.rogue.shopcontrol.utils.capitalizarPrimeiraLetra
import com.rogue.shopcontrol.utils.formatDataCompra
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

private const val LOTE_TAMANHO = 12
private const val DIA_FIXO = 10
private const val LIMIAR_EXTENSAO_MESES = 2L

class RendaRepository(
    private val rendaDao: RendaDao
) {

    fun getRendaComDados(): Flow<List<RendaComDados>> =
        rendaDao.getRendaComDados()


    fun getAllRendas(): Flow<List<RendaEntity>> =
        rendaDao.getAllRaw()


    suspend fun criarRendaAvulsa(
        descricao: String,
        valor: Double,
        data: LocalDate,
        categoriaId: Long,
        fonteId: Long
    ): Long =

        rendaDao.insert(
            RendaEntity(
                descricao = capitalizarPrimeiraLetra(descricao),
                valor = valor,
                data = formatDataCompra(data),
                rendaCategoriaId = categoriaId,
                fonteRendaId = fonteId,
                recorrente = false,
                origemRecorrenteId = null
            )
        )


    suspend fun deleteRendaById(
        rendaId: Long
    ) {
        rendaDao.deleteByIds(listOf(rendaId))
    }


    suspend fun saveRenda(
        rendaId: Long,
        descricao: String,
        valor: Double,
        data: LocalDate,
        categoriaId: Long,
        fonteId: Long,
        recorrente: Boolean
    ): Long {

        val descricao = capitalizarPrimeiraLetra(descricao)

        if (rendaId == 0L) {

            val novoId =
                rendaDao.insert(
                    RendaEntity(
                        descricao = descricao,
                        valor = valor,
                        data = formatDataCompra(data),
                        rendaCategoriaId = categoriaId,
                        fonteRendaId = fonteId,
                        recorrente = recorrente,
                        origemRecorrenteId = null
                    )
                )

            if (recorrente) {

                gerarLote(
                    descricao = descricao,
                    valor = valor,
                    categoriaId = categoriaId,
                    fonteId = fonteId,
                    dataBase = data,
                    idAnterior = novoId
                )

            }

            return novoId

        } else {

            collectForwardChain(rendaId).forEach { id ->

                rendaDao.updateCampos(
                    id = id,
                    descricao = descricao,
                    valor = valor,
                    categoriaId = categoriaId,
                    fonteId = fonteId
                )

            }

            return rendaId

        }

    }


    suspend fun deleteRendaForward(
        rendaId: Long
    ) {

        val alvo =
            rendaDao.findByIdOnce(rendaId) ?: return

        val cadeia =
            collectForwardChain(rendaId)

        alvo.origemRecorrenteId?.let { parentId ->
            rendaDao.setRecorrente(parentId, false)
        }

        rendaDao.deleteByIds(cadeia)

    }


    suspend fun verificarEEstenderSeries() {

        val tails =
            rendaDao.findTailsAtivas()

        tails.forEach { tail ->

            val dataTail =
                parseDataCompra(tail.data) ?: return@forEach

            if (dataTail.isBefore(LocalDate.now().plusMonths(LIMIAR_EXTENSAO_MESES))) {

                gerarLote(
                    descricao = tail.descricao,
                    valor = tail.valor,
                    categoriaId = tail.rendaCategoriaId,
                    fonteId = tail.fonteRendaId,
                    dataBase = dataTail,
                    idAnterior = tail.id
                )

            }

        }

    }


    private suspend fun collectForwardChain(
        startId: Long
    ): List<Long> {

        val resultado = mutableListOf(startId)
        var idAtual = startId

        while (true) {

            val filho =
                rendaDao.findByOrigemRecorrenteId(idAtual) ?: break

            resultado.add(filho.id)
            idAtual = filho.id

        }

        return resultado

    }


    private suspend fun gerarLote(
        descricao: String,
        valor: Double,
        categoriaId: Long,
        fonteId: Long,
        dataBase: LocalDate,
        idAnterior: Long
    ) {

        var mesAtual = YearMonth.from(dataBase).plusMonths(1)
        var ultimoId = idAnterior

        repeat(LOTE_TAMANHO) {

            val novaData = mesAtual.atDay(DIA_FIXO)

            ultimoId =
                rendaDao.insert(
                    RendaEntity(
                        descricao = descricao,
                        valor = valor,
                        data = formatDataCompra(novaData),
                        rendaCategoriaId = categoriaId,
                        fonteRendaId = fonteId,
                        recorrente = true,
                        origemRecorrenteId = ultimoId
                    )
                )

            mesAtual = mesAtual.plusMonths(1)

        }

    }

}
