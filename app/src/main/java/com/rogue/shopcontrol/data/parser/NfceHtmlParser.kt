package com.rogue.shopcontrol.data.parser

import com.rogue.shopcontrol.domain.model.NfceData
import org.jsoup.Jsoup

import com.rogue.shopcontrol.domain.model.*
import org.jsoup.nodes.Document


class NfceHtmlParser {


    fun parse(
        html: String
    ): NfceData {


        val document =
            Jsoup.parse(html)


        return NfceData(

            chaveAcesso =
                extractChave(document),

            estabelecimento =
                extractEstabelecimento(document),

            numeroNota =
                extractNumero(document),

            dataEmissao =
                extractEmissao(document),

            valorTotal =
                extractValorTotal(document),

            produtos =
                mergeProdutosDuplicados(
                    extractProdutos(document)
                )

        )

    }


    private fun mergeProdutosDuplicados(
        produtos: List<NfceProduct>
    ): List<NfceProduct> {

        return produtos
            .groupBy {
                it.nome to it.valorUnitario
            }
            .map { (_, grupo) ->

                grupo.first().copy(
                    quantidade = grupo.sumOf { it.quantidade },
                    valorTotal = grupo.sumOf { it.valorTotal }
                )

            }

    }


    private fun extractEstabelecimento(
        document: Document
    ): NfceEstablishment {


        val content =
            document.selectFirst("#u20")
                ?.parent()


        val nome =
            document
                .selectFirst("#u20")
                ?.text()
                ?: ""


        val dados =
            content
                ?.select(".text")
                ?.map {
                    it.text()
                }
                ?: emptyList()


        val cnpj =
            dados
                .firstOrNull()
                ?.replace("CNPJ:", "")
                ?.trim()
                ?: ""


        val endereco =
            dados
                .getOrNull(1)
                ?: ""


        return NfceEstablishment(
            nome = nome,
            cnpj = cnpj,
            endereco = endereco
        )

    }


    private fun extractProdutos(
        document: Document
    ): List<NfceProduct> {


        return document
            .select("#tabResult tr")
            .map {


                val descricao =
                    it.selectFirst(".txtTit")
                        ?.text()
                        ?: ""


                val codigo =
                    it.selectFirst(".RCod")
                        ?.text()
                        ?.replace(
                            Regex("[^0-9]"),
                            ""
                        )


                val quantidade =
                    it.selectFirst(".Rqtd")
                        ?.text()
                        ?.replace(
                            Regex("[^0-9,]"),
                            ""
                        )
                        ?.replace(",", ".")
                        ?.toDoubleOrNull()
                        ?: 0.0


                val unidade =
                    it.selectFirst(".RUN")
                        ?.text()
                        ?.replace("UN:", "")
                        ?.trim()


                val valorUnitario =
                    it.selectFirst(".RvlUnit")
                        ?.text()
                        ?.replace(
                            Regex("[^0-9,]"),
                            ""
                        )
                        ?.replace(",", ".")
                        ?.toDoubleOrNull()
                        ?: 0.0


                val valorTotal =
                    it.selectFirst(".valor")
                        ?.text()
                        ?.replace(",", ".")
                        ?.toDoubleOrNull()
                        ?: 0.0


                NfceProduct(
                    codigo = codigo,
                    nome = descricao,
                    quantidade = quantidade,
                    unidade = unidade,
                    valorUnitario = valorUnitario,
                    valorTotal = valorTotal
                )

            }

    }


    private fun extractValorTotal(
        document: Document
    ): Double {


        return document
            .select("#totalNota .txtMax")
            .text()
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0

    }


    private fun extractChave(
        document: Document
    ): String {


        return document
            .selectFirst(".chave")
            ?.text()
            ?.replace(
                " ",
                ""
            )
            ?: ""

    }


    private fun extractNumero(
        document: Document
    ): String? {


        val texto =
            document
                .select("#infos")
                .text()


        return Regex(
            "Número:\\s*(\\d+)"
        )
            .find(texto)
            ?.groupValues
            ?.get(1)

    }


    private fun extractEmissao(
        document: Document
    ): String? {


        val texto =
            document
                .select("#infos")
                .text()


        return Regex(
            "Emissão:\\s*(.*?)\\s*-"
        )
            .find(texto)
            ?.groupValues
            ?.get(1)

    }

}