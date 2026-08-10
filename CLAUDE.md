# ShopControl — Contexto do Projeto

> Este arquivo é lido automaticamente pelo Claude Code ao abrir este diretório. Ele documenta o estado do app até o momento, para que qualquer nova conversa (neste ou em outro chat) tenha contexto completo sem precisar re-explorar tudo do zero.

## O que é o app

App Android pessoal (não publicado) para controle de gastos de compras via leitura do QR Code da **NFC-e** (nota fiscal de consumidor eletrônica, SEFAZ-SP). Ao escanear, o app baixa o HTML da nota, faz parsing e salva estabelecimento, compra e produtos localmente. Em cima disso, oferece análises de gasto (mês, produto, categoria, estabelecimento).

- Repositório: https://github.com/GabiNandes09/shop_control
- Único desenvolvedor/usuário: o próprio dono do repo, testando no próprio celular (Xiaomi M2101K6I) conectado via ADB wireless.

## Stack técnica

- Kotlin 2.3.10, Jetpack Compose (Material3), sem XML views (só o `MainActivity` + tema pré-Compose para status bar).
- **Koin** 4.2.2 para DI (não Hilt/Dagger).
- **Room** 2.8.4 para persistência local (SQLite), banco `shop_control.db`, **versão 3** (migrations reais, ver abaixo — nunca usar `fallbackToDestructiveMigration`, sempre migração SQL preservando dados).
- **Retrofit + OkHttp + kotlinx.serialization** para buscar o HTML da NFC-e (`NfceApi`, base URL da SEFAZ-SP).
- **Jsoup** para parsear o HTML da nota (`NfceHtmlParser`).
- **CameraX + ML Kit Barcode Scanning** para o scanner de QR Code (`utils/QrCodeScanner.kt`).
- **Navigation Compose** para navegação (`AppNavigation.kt`), com bottom bar de 3 abas.
- `material-icons-extended` (não só `-core`, pois ícones como `CameraAlt` não estão no core).
- `minSdk 26`, `compileSdk/targetSdk` recentes (37/36).

## Arquitetura (Clean Architecture simplificada)

```
data/
  local/
    dao/         — interfaces Room (@Dao)
    database/    — AppDatabase, AppDatabaseMigrations
    entity/      — @Entity (tabelas) + POJOs de resultado de query (não são tabelas, ex: ProdutoGasto, EstabelecimentoGasto, ProdutoItemComData)
  remote/        — NfceApi (Retrofit)
  parser/        — NfceHtmlParser (Jsoup)
  repository/    — um repository por agregado: CompraRepository, ProdutoRepository, CategoriaRepository, EstabelecimentoRepository
domain/
  model/         — modelos de domínio puros (NfceData, MonthlySpending)
  usecase/       — uma classe por operação (padrão estabelecido: NÃO agrupar operações em um usecase só, mesmo que pareçam relacionadas — decisão explícita do usuário)
presentation/
  components/    — composables reutilizáveis (ver lista abaixo)
  screens/       — uma tela por arquivo, recebe callbacks de navegação como parâmetros
  viewmodel/     — um ViewModel por tela + states/ com os data class de estado
  navigation/    — Routes.kt (rotas seladas) + AppNavigation.kt (NavHost + bottom bar)
  model/         — ChartEntry (modelo de apresentação genérico p/ gráficos)
di/              — AppModule (repos + usecases), DatabaseModule (Room), ViewModelModule
ui/theme/        — Color.kt, Theme.kt, Type.kt
utils/           — formatCurrency, parseDataCompra/formatMonthYear (DateUtils), toChartEntry (ChartMappers), startScanner
```

**Convenções importantes já validadas com o usuário:**
- Um `UseCase` por operação, mesmo que dois usecases sejam muito parecidos (ex.: `GetComprasUseCase` e `GetCompraByIdUseCase` ficam separados — decisão explícita, não juntar).
- Filtros (mês, nome, categoria, ordenação) são sempre aplicados **client-side** no ViewModel (cache da lista "original" + função `aplicarFiltros()`), não em múltiplas queries SQL — mais simples de combinar vários filtros.
- Datas são `String` no formato `dd/MM/yyyy HH:mm:ss` (como a NFC-e retorna), parseadas via `utils/parseDataCompra` → `LocalDate`. Não há coluna de data real no banco.
- Toda tela com filtros usa o componente `FilterToggleChip` + `FilterOverlay` (chip "Filtros" que abre um painel **flutuante sobre o conteúdo**, não empurra a lista pra baixo — decisão explícita do usuário).
- Toda tela usa `ScreenHeader` (título + botão de voltar opcional). As 3 abas da bottom bar (Home/Registros/Configurações) **não** passam `onBackClick` (são destinos de nível superior, sem seta de voltar).

## Banco de dados

Entidades (`@Entity`): `EstabelecimentoEntity` (nome, cnpj, endereco, **apelido** nullable), `ProdutoEntity` (nome, codigo, categoriaId — `0` = sem categoria, sentinela, não FK), `CategoriaEntity` (nome), `CompraEntity` (estabelecimentoId, dataCompra, valorTotal, chaveNfce), `ItemCompraEntity` (compraId, produtoId, quantidade, valorUnitario, valorTotal).

Migrations (histórico, nunca destrutivas):
- v1→v2: adiciona coluna `apelido` em `estabelecimentos`.
- v2→v3: funde estabelecimentos duplicados (mesmo CNPJ) que existiam de um bug antigo — reaponta `compras.estabelecimentoId` pro registro mais antigo e apaga os duplicados (só para CNPJ não-vazio).

**Deduplicação na gravação** (`CompraRepository.save`): reaproveita estabelecimento existente por CNPJ e produto existente por nome (em vez de sempre inserir novo). Também detecta compra já salva (mesmo CNPJ + data/hora) e evita duplicar. Itens duplicados dentro da mesma nota (mesmo nome + mesmo valor unitário) são somados no parser antes de salvar.

**Nota importante sobre `ProdutoEntity.codigo`**: é o código do produto como aparece na NFC-e (`.RCod`), que é o código **interno do estabelecimento**, não necessariamente o código de barras/EAN real (frequentemente coincidem na prática, mas não é garantido). Não há campo dedicado de código de barras nem validação de EAN.

## Navegação e telas

Bottom bar (3 abas, sempre visível nelas, oculta nas demais telas): **Início** (Home), **Registros** (ícone de carrinho), **Configurações**.

- **Home**: FAB de câmera (scanner) + `StatCard` "Valor gasto" (mês atual, com mês no subtítulo) → abre comparação mensal + card "Produtos" (top 5 por gasto) → abre lista completa.
- **Registros**: lista de compras, filtro de mês (flutuante), clique abre detalhe da compra (resumo + itens + excluir).
- **Configurações**: botões para Produtos (catálogo simples, com busca), Categorias (CRUD + botão "Gasto por Categoria" dentro dela), Estabelecimentos (ranking por gasto, com busca).
- **Scanner**: lê QR Code, busca HTML da NFC-e, salva, mostra diálogo de sucesso, navega pro detalhe da compra salva (com `popUpTo` removendo o Scanner da pilha).
- **Detalhe de produto**: resumo (qtd, preço médio, maior/menor preço pago), categoria (botão "+" pra atribuir, 1 categoria por produto), histórico de compras daquele produto.
- **Detalhe de estabelecimento**: resumo (CNPJ, endereço, total gasto, **apelido editável** via lápis), gráfico de gasto mês a mês, lista de compras naquele estabelecimento (filtro de mês).
- **AnalyticsScreen**: componente de tela genérico (título + `List<ChartEntry>` + loading) reaproveitado 3x — comparação mensal geral, gasto por estabelecimento (mensal) e gasto por categoria. `AnalyticsContent`/`analyticsChartItems` são extraídos para poder compor dentro de outras `LazyColumn` (ex. detalhe de estabelecimento mistura resumo + gráfico + lista de compras numa única lista rolável).

## Tema e idioma

- **Preto e dourado** em todo o app (`ui/theme/Color.kt`/`Theme.kt`), tema único (sem alternância claro/escuro, dynamic color desativado — decisão explícita).
- `MainActivity` envolve `AppNavigation()` num `Surface(color = MaterialTheme.colorScheme.background)` — **sem isso, texto sem cor explícita renderiza preto sobre fundo preto** (causa raiz de um bug já corrigido; não remover esse `Surface`).
- **i18n**: todas as strings em `res/values/strings.xml` (**português, é o idioma padrão**) + `res/values-en/strings.xml` (inglês). Qualquer texto novo na UI deve ir para os dois arquivos, nunca hardcoded no Kotlin.

## Fluxo de build (sempre seguir isso)

- **Sempre compilar e instalar no dispositivo conectado após qualquer alteração de código**, sem esperar o usuário pedir: `adb devices -l` pra confirmar conexão, depois `./gradlew.bat :app:installDebug --console=plain`.
- O daemon do Gradle às vezes é derrubado externamente no meio do build ("Daemon is stopping immediately stop command received") — se isso acontecer, repetir o comando com `--no-daemon` costuma resolver.
- Rodar em background (`run_in_background`) para builds grandes, já que costumam levar 1-2 min.
- Se o dispositivo não estiver conectado (`adb devices -l` vazio), compilar mesmo assim (`compileDebugKotlin`) pra validar o código, e avisar que falta instalar.

## Git

- Commits em português, prefixo `feat:` (convenção usada até agora, ainda sem `fix:`/`chore:` separados — tudo tem ido como `feat:`).
- Corpo do commit com bullets resumindo as mudanças.
- Só commitar/push quando o usuário pedir explicitamente ("commit e suba").
- `.gitignore` ignora `*.apk` (não versionar builds gerados).

## O que NÃO existe ainda (ideias já discutidas, não implementadas)

- Backup/exportação de dados (tudo é local; sem isso, perder o celular = perder tudo).
- Cadastro manual de compra (sem NFC-e — feira, ambulante).
- Alerta de aumento de preço (comparar valor unitário novo vs. última compra do mesmo produto).
- Orçamento mensal com alerta.
- Sugestão automática de categoria / categorização em lote.
- Campo de código de barras real (hoje só existe o código interno da nota, ver seção "Banco de dados").
