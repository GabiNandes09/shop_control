package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.repository.ARecebimentoRepository
import com.rogue.shopcontrol.data.repository.BackupRepository
import com.rogue.shopcontrol.data.repository.CategoriaRepository
import com.rogue.shopcontrol.data.repository.CompraRepository
import com.rogue.shopcontrol.data.repository.EstabelecimentoRepository
import com.rogue.shopcontrol.data.repository.FonteRendaRepository
import com.rogue.shopcontrol.data.repository.HomeDateRangeRepository
import com.rogue.shopcontrol.data.repository.ProdutoRepository
import com.rogue.shopcontrol.data.repository.RendaCategoriaRepository
import com.rogue.shopcontrol.data.repository.RendaRepository
import com.rogue.shopcontrol.domain.usecase.AddCategoriaERetornarIdUseCase
import com.rogue.shopcontrol.domain.usecase.AddCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.AddEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.AddFonteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.AddRendaCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.AssignCategoriaToProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.CreateProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteARecebimentoForwardUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCompraForwardUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteRendaCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteRendaUseCase
import com.rogue.shopcontrol.domain.usecase.ExportBackupUseCase
import com.rogue.shopcontrol.domain.usecase.FindProdutoByNameUseCase
import com.rogue.shopcontrol.domain.usecase.GetARecebimentosComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllEstabelecimentosUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllFontesRendaUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.GetBalancoMensalHistoricoUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasByEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.domain.usecase.GetDivisaoContaUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoMonthlyHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetFonteRendaByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetHistoricoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetParcelasDaSerieUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoGastoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.GetTodasComprasComEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetTodosPrecosProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.ImportBackupUseCase
import com.rogue.shopcontrol.domain.usecase.LinkEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.LinkProdutosToCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.MarcarARecebimentoComoPagoUseCase
import com.rogue.shopcontrol.domain.usecase.MergeProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.SaveARecebimentoUseCase
import com.rogue.shopcontrol.domain.usecase.SaveFixaOuParceladaUseCase
import com.rogue.shopcontrol.domain.usecase.SaveHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.SaveManualPurchaseUseCase
import com.rogue.shopcontrol.domain.usecase.SavePurchaseUseCase
import com.rogue.shopcontrol.domain.usecase.SaveRendaUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCategoriaEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCodigoBarrasProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateGrupoCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateRendaCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesARecebimentoUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesFixaUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesRendaUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        CompraRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        ProdutoRepository(
            get()
        )
    }
    single {
        CategoriaRepository(
            get()
        )
    }
    single {
        EstabelecimentoRepository(
            get(),
            get()
        )
    }
    single {
        HomeDateRangeRepository(
            androidContext()
        )
    }
    single {
        BackupRepository(
            androidContext(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        RendaRepository(
            get()
        )
    }
    single {
        RendaCategoriaRepository(
            get()
        )
    }
    single {
        FonteRendaRepository(
            get()
        )
    }
    single {
        ARecebimentoRepository(
            get(),
            get()
        )
    }
    factory {
        SavePurchaseUseCase(
            get()
        )
    }
    factory {
        GetComprasUseCase(
            get()
        )
    }
    factory {
        GetCompraByIdUseCase(
            get()
        )
    }
    factory {
        DeleteCompraUseCase(
            get()
        )
    }
    factory {
        GetMonthlySpendingHistoryUseCase(
            get()
        )
    }
    factory {
        GetProdutoGastoByIdUseCase(
            get()
        )
    }
    factory {
        GetHistoricoProdutoUseCase(
            get()
        )
    }
    factory {
        GetProdutoItensComDataUseCase(
            get()
        )
    }
    factory {
        GetAllProdutosUseCase(
            get()
        )
    }
    factory {
        AssignCategoriaToProdutoUseCase(
            get()
        )
    }
    factory {
        GetCategoriasUseCase(
            get()
        )
    }
    factory {
        AddCategoriaUseCase(
            get()
        )
    }
    factory {
        DeleteCategoriaUseCase(
            get()
        )
    }
    factory {
        GetTodasComprasComEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetEstabelecimentoByIdUseCase(
            get()
        )
    }
    factory {
        GetEstabelecimentoMonthlyHistoryUseCase(
            get()
        )
    }
    factory {
        UpdateApelidoEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetComprasByEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        UpdateCategoriaUseCase(
            get()
        )
    }
    factory {
        GetHomeDateRangeUseCase(
            get()
        )
    }
    factory {
        SaveHomeDateRangeUseCase(
            get()
        )
    }
    factory {
        GetAllEstabelecimentosUseCase(
            get()
        )
    }
    factory {
        FindProdutoByNameUseCase(
            get()
        )
    }
    factory {
        CreateProdutoUseCase(
            get()
        )
    }
    factory {
        SaveManualPurchaseUseCase(
            get()
        )
    }
    factory {
        LinkEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetTodosPrecosProdutoUseCase(
            get()
        )
    }
    factory {
        UpdateCodigoBarrasProdutoUseCase(
            get()
        )
    }
    factory {
        UpdateApelidoProdutoUseCase(
            get()
        )
    }
    factory {
        MergeProdutosUseCase(
            get()
        )
    }
    factory {
        ExportBackupUseCase(
            get()
        )
    }
    factory {
        ImportBackupUseCase(
            get()
        )
    }
    factory {
        AddEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetRendaComDadosUseCase(
            get()
        )
    }
    factory {
        SaveRendaUseCase(
            get()
        )
    }
    factory {
        DeleteRendaUseCase(
            get()
        )
    }
    factory {
        VerificarEEstenderSeriesRendaUseCase(
            get()
        )
    }
    factory {
        GetRendaCategoriasUseCase(
            get()
        )
    }
    factory {
        AddRendaCategoriaUseCase(
            get()
        )
    }
    factory {
        UpdateRendaCategoriaUseCase(
            get()
        )
    }
    factory {
        DeleteRendaCategoriaUseCase(
            get()
        )
    }
    factory {
        GetAllFontesRendaUseCase(
            get()
        )
    }
    factory {
        AddFonteRendaUseCase(
            get()
        )
    }
    factory {
        GetFonteRendaByIdUseCase(
            get()
        )
    }
    factory {
        SaveFixaOuParceladaUseCase(
            get()
        )
    }
    factory {
        DeleteCompraForwardUseCase(
            get()
        )
    }
    factory {
        VerificarEEstenderSeriesFixaUseCase(
            get()
        )
    }
    factory {
        GetParcelasDaSerieUseCase(
            get()
        )
    }
    factory {
        GetComprasComDataUseCase(
            get()
        )
    }
    factory {
        GetBalancoMensalHistoricoUseCase(
            get(),
            get()
        )
    }
    factory {
        GetARecebimentosComDadosUseCase(
            get()
        )
    }
    factory {
        SaveARecebimentoUseCase(
            get()
        )
    }
    factory {
        DeleteARecebimentoForwardUseCase(
            get()
        )
    }
    factory {
        MarcarARecebimentoComoPagoUseCase(
            get()
        )
    }
    factory {
        VerificarEEstenderSeriesARecebimentoUseCase(
            get()
        )
    }
    factory {
        AddCategoriaERetornarIdUseCase(
            get()
        )
    }
    factory {
        UpdateGrupoCategoriaUseCase(
            get()
        )
    }
    factory {
        LinkProdutosToCategoriaUseCase(
            get()
        )
    }
    factory {
        UpdateCategoriaEstabelecimentoUseCase(
            get()
        )
    }
    factory {
        GetDivisaoContaUseCase(
            get()
        )
    }
}