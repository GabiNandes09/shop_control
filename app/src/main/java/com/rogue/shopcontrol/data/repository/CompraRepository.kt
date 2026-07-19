package com.rogue.shopcontrol.data.repository

import com.rogue.shopcontrol.data.local.dao.CompraDao
import com.rogue.shopcontrol.data.local.dao.EstabelecimentoDao
import com.rogue.shopcontrol.data.local.dao.ProdutoDao
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.ItemCompraEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.domain.model.NfceData

class CompraRepository(
    private val estabelecimentoDao: EstabelecimentoDao,
    private val produtoDao: ProdutoDao,
    private val compraDao: CompraDao
) {


    suspend fun save(
        data: NfceData
    ) {


        val estabelecimentoId =
            estabelecimentoDao.insert(
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
                    valorTotal = data.valorTotal
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

    }

}