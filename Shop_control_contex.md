# ShopControl — Resumo de Funcionalidades

> Documento de contexto funcional do app, atualizado em 2026-08-31. Complementa o `CLAUDE.md` (que foca em arquitetura técnica) com uma visão das funcionalidades do ponto de vista do usuário.
>
> Nesta atualização: adicionadas as seções **Categoria por Estabelecimento** e **Divisão de Conta**, as 2 features da terceira rodada de planejamento.

## 📷 Captura de compras
- **Scanner de QR Code** (câmera, CameraX + ML Kit) lê o QR Code da NFC-e, busca o HTML da nota na SEFAZ-SP e extrai automaticamente: estabelecimento (nome, CNPJ, endereço), data/hora, valor total e todos os produtos (nome, código, quantidade, valor unitário, valor total).
- **Deduplicação automática** ao salvar:
  - Mesmo CNPJ + mesma data/hora → não duplica a compra.
  - Mesmo CNPJ → reaproveita o estabelecimento já cadastrado (em vez de criar um novo).
  - Mesmo nome de produto → reaproveita o produto já cadastrado.
  - Itens repetidos dentro da mesma nota (mesmo nome + mesmo valor unitário) são somados em um único item.
- Ao salvar com sucesso, mostra um diálogo de confirmação e leva direto para o detalhe da compra.
- Enquanto a nota é buscada e lida, aparece um indicador de carregamento sobre a câmera. Se o QR Code for inválido ou a nota não for encontrada (ex. rejeitada pela própria Sefaz), aparece um aviso de erro em vez de salvar uma compra vazia.

## 🏠 Tela inicial (Home)
- Bottom bar fixa com 5 abas, **Início centralizado**: Registros, Renda, **Início**, Análise, Configurações.
- Filtro por período — é a única tela que **lembra o período escolhido** entre sessões do app, e é a partir dela que todas as outras telas do app começam (ver "Personalização" abaixo). Ao lado do filtro, um **sino de notificação** com uma bolinha mostrando quantos "A Receber" estão em atraso — toca nele pra ver a lista e ir direto pra um item.
- Card **"Saldo"**, único card da tela agora — Renda menos Despesas do período filtrado, em verde ou vermelho conforme o sinal, com o detalhamento embaixo ("Renda R$X − Despesas R$Y"); clicável, leva ao **Balanço**. Os cards "Valor gasto", "Produtos" e "Estabelecimentos" saíram da Home — foram pra aba **Análise** (ver abaixo).
- FAB de câmera abre um menu: "Escanear NFC-e" ou "Adicionar manualmente".

## 📈 Análise
- Nova aba na barra inferior. Card de entrada pra **"Comparação de Gastos"** (gráfico dos últimos 6 meses) no topo.
- Card **"Produtos"** com os 5 produtos em que mais se gastou no período (botão de seta leva à lista completa) e card **"Estabelecimentos"** com os 5 estabelecimentos onde mais se gastou no período (item clicável abre o detalhe direto, seta leva à lista completa) — os mesmos cards que antes ficavam na Home.
- Tem seu próprio filtro de período (começa igual ao da Home, mas pode ser mudado aqui sem afetar a Home).

## 🧾 Registros (compras)
- Lista de todas as compras salvas, com filtro por período (calendário, painel flutuante minimizável) **e por tipo** (Todas / Variáveis / Fixas / Parceladas / Rápidas).
- Cada compra mostra estabelecimento (ou apelido, se definido), data e valor total. Uma compra Fixa, Parcelada ou Rápida mostra também o nome da despesa em destaque.
- **Detalhe da compra**: resumo (estabelecimento, CNPJ, endereço, data, total; CNPJ/endereço ficam ocultos e um selo "Cadastro manual" aparece quando a compra não veio de NFC-e) + lista de itens comprados, cada um clicável (leva ao detalhe do produto) e com uma seta verde/vermelha quando o preço unitário mudou em relação à última compra do mesmo produto + botão de **excluir** (com confirmação). Compras manuais ganham também um botão **"Editar"** no topo. Compras Fixas/Parceladas mostram no lugar dos itens um card com nome/categoria/valor e, se parcelada, a lista de todas as parcelas da série com a parcela atual em destaque.
- FAB próprio ("+") para "Adicionar manualmente" direto dessa tela.

## 💰 Renda
- Aba própria na bottom bar, com filtro por período (calendário) e lista de entradas de dinheiro (salário, freela, presente, etc.).
- FAB "+" abre um formulário: descrição, valor, categoria (escolhida de uma lista própria de categorias de renda — separada das categorias de produto), fonte (autocomplete que também permite cadastrar uma fonte nova na hora) e, só ao criar, data e um interruptor **"Recorrente"**.
- **Renda recorrente**: ao marcar recorrente, o app já gera automaticamente as próximas ocorrências mensais e continua estendendo a série sozinho conforme o tempo passa — o usuário nunca precisa "renovar" manualmente. Editar uma ocorrência recorrente aplica a mudança nela e em todas as futuras já geradas; excluir remove essa e as futuras, e a série para de se repetir a partir dali.
- Editar/excluir uma entrada avulsa (não recorrente) afeta só ela.
- Em Configurações: **"Fontes"** (ranking de quanto já foi recebido de cada fonte, com busca e FAB "+" para cadastrar direto; clique abre o detalhe com os lançamentos daquela fonte no período) e **"Categorias de Renda"** (CRUD igual ao de categorias de produto: adicionar, listar, renomear, excluir).

## 📌 Despesas Fixas, Parceladas e Rápidas
- Ao cadastrar uma compra manualmente (pelos mesmos pontos de entrada de sempre), é possível escolher entre quatro tipos: **Variável** (o cadastro de sempre, com lista de itens), **Fixa** (uma despesa recorrente com valor único, ex. aluguel ou assinatura), **Parcelada** (uma despesa dividida em N parcelas mensais, ex. um eletrodoméstico em 10x) e **Rápida** (uma despesa avulsa, sem itemizar, ex. um rolê de R$100 num bar).
- **Fixa**: nome, valor e categoria. Funciona como a Renda recorrente — o app gera e mantém sozinho as próximas ocorrências mensais.
- **Parcelada**: nome, valor de cada parcela, categoria e número total de parcelas. É possível informar quantas parcelas já foram pagas, para lançar retroativamente uma compra que começou antes (ex. já pagou 3 das 10 parcelas). A data mostrada é sempre a data original da compra; internamente cada parcela "pertence" ao seu próprio mês, então elas aparecem espalhadas corretamente nos filtros de período (mês a mês) mesmo compartilhando a mesma data exibida.
- **Rápida**: nome, valor e categoria — igual à Fixa, mas um lançamento único, sem repetir todo mês. Pensada pra despesas pontuais que não valem a pena itemizar produto a produto (o valor e o local mudam a cada vez), sem misturar esses nomes com o catálogo de produtos.
- O tipo de uma compra não pode ser trocado depois de salva (só os outros campos são editáveis).
- Excluir uma Fixa ou Parcelada avisa que a exclusão remove essa ocorrência/parcela e todas as futuras já geradas, e a série para de continuar. Uma Rápida exclui direto (não tem série).
- Uma categoria (de produto) que já foi usada numa despesa Fixa/Parcelada/Rápida também não pode mais ser excluída (antes só bloqueava por uso em produto).
- Nova tela em Configurações, **"Compras Parceladas"**: lista todas as séries parceladas, mostra a parcela atual de cada uma ("X/Y"), o total comprometido por mês, e separa em Ativas/Concluídas (com opção de mostrar as concluídas).
- Nos totais da Home e no gráfico "Gasto por Categoria", as despesas Fixas, Parceladas e Rápidas entram na soma normalmente, junto com as compras de itens.

## ✍️ Cadastro manual de compra (sem NFC-e)
- Pensado para compras informais sem nota fiscal (feira, ambulante). Dois pontos de entrada: menu do FAB da Home ("Escanear NFC-e" / "Adicionar manualmente") e um FAB próprio na aba Registros.
- Fluxo: nome do estabelecimento (com autocomplete dos já cadastrados, sem exigir CNPJ), apelido opcional (só quando o estabelecimento é novo), data da compra (calendário, aceita datas retroativas) e lista de itens.
- Cada item: nome do produto com autocomplete (pede confirmação antes de cadastrar um produto novo), quantidade e valor unitário **ou** valor total — o outro é calculado automaticamente. Não deixa adicionar o mesmo produto duas vezes na mesma compra.
- **Categoria da compra**: ao escolher um estabelecimento que já tem categoria definida, ela é preenchida automaticamente — mas pode ser trocada na hora ou depois, direto no formulário. Sem categoria no estabelecimento, a compra fica "Sem categoria" até alguém definir uma.
- Depois de salva, a compra manual pode ser **editada a qualquer momento** (data, estabelecimento, categoria, adicionar/remover/editar itens) pelo botão "Editar" no topo do detalhe da compra — diferente das compras por NFC-e, que só podem ser excluídas. Um selo "Cadastro manual" identifica essas compras, e CNPJ/endereço (que não existem) ficam ocultos.
- Um estabelecimento cadastrado manualmente (sem CNPJ) pode depois ser **vinculado** a um estabelecimento com CNPJ (ex. quando o usuário escaneia uma NFC-e real do mesmo local) — uma ação irreversível, com confirmação, que funde todo o histórico de compras no estabelecimento com CNPJ. Se os dois tiverem categorias diferentes, o app pergunta qual das duas deve prevalecer antes de confirmar.

## 🤝 Divisão de Conta
- Disponível ao cadastrar uma compra manualmente (Variável, Fixa, Parcelada ou Rápida): um interruptor **"Dividir conta"** no formulário, visível só na criação (uma vez salva, a divisão não pode mais ser adicionada, removida ou alterada).
- Ao ativar, aparece: lista de participantes (autocomplete de pessoas já usadas em Renda/A Receber, com opção de cadastrar uma nova na hora), interruptor **"Dividir igualmente"** (recalcula automaticamente a parte de cada um sempre que alguém é adicionado/removido, ou quando o interruptor é ligado) e um campo de valor editável por participante — mostra em tempo real quanto sobra como "Sua parte".
- Cada participante vira um lançamento em **"A Receber"** com a parte dele, descrição automática ("Sua parte em `<estabelecimento>`, `<data>`") e a mesma categoria da despesa original. Nos itens da compra (quando Variável), o preço de cada produto continua cheio — só o total da compra reflete a sua parte.
- Numa despesa Fixa ou Parcelada dividida, cada ocorrência/parcela futura (inclusive as geradas automaticamente mês a mês) recebe sua própria cobrança pros participantes. Excluir uma ocorrência remove os "A Receber" pendentes ligados a ela, mas preserva os que já foram pagos.
- No detalhe da compra, uma seção "Divisão da conta" mostra quem deve quanto.

## 📦 Produtos
- **"Produtos" (top 5)** na aba Análise, e tela completa **"Todos os Produtos"** com filtros combináveis: período, busca por nome (ou apelido), ordenar por valor gasto ou quantidade, e filtrar por categoria.
- Todo card de produto em lista (Análise, Todos os Produtos, Produtos Cadastrados) mostra dois iconezinhos: um indicando se o produto já tem código de barras cadastrado e outro se já tem categoria — coloridos quando tem, apagados quando não tem. Ajuda a identificar de relance quais produtos ainda faltam completar.
- **Detalhe do produto**: quantidade total comprada, preço médio, **maior e menor valor pago**, total gasto, categoria atual (com botão "+" para atribuir/trocar — um produto tem no máximo uma categoria), **código de barras (EAN)** editável (digitação ou câmera), **apelido** editável, e histórico completo de compras daquele produto (com data, estabelecimento e preço de cada compra). O menu de opções (ícone de engrenagem no topo da tela) tem **"Vincular a outro produto"** (mescla definitivamente dois produtos que na prática são o mesmo item — irreversível, com confirmação).
- **Apelido de produto**: quando definido, aparece em todo o app (Home, Registros, detalhe de compra, Todos os Produtos, Produtos Cadastrados etc.) no lugar do nome, com o nome real pequeno embaixo — mesmo padrão do apelido de estabelecimento.
- **Produtos Cadastrados** (em Configurações): catálogo simples de todos os produtos já registrados, com busca por nome (ou apelido) — pensado para navegar direto ao detalhe de um produto sem passar pelas métricas de gasto.

## 🏷️ Categorias
- CRUD completo em Configurações → Categorias: FAB "+" abre o diálogo de nova categoria, listar (com busca por nome), renomear e excluir. Nomes são únicos (checagem case-insensitive).
- **Escolher/trocar categoria de um produto** (ou de uma despesa Fixa/Parcelada/Rápida): o seletor agora tem busca por nome, e se o nome digitado não existir ainda, aparece a opção "Criar categoria" — cria e já aplica, sem precisar sair da tela.
- **Agrupar categorias**: qualquer categoria pode virar "grupo" de outras (ex. "Alface", "Queijo" e "Água mineral" agrupadas sob "Itens de mercado") — ícone próprio em cada categoria abre o seletor (com busca) pra escolher o grupo; o grupo escolhido aparece como subtítulo na lista.
- **Vincular vários produtos de uma vez**: ícone de "vincular" em cada categoria abre uma lista com checkbox de todos os produtos (com busca, já que o catálogo pode ser grande) — marca/desmarca e salva de uma vez, sem precisar abrir produto por produto.
- **Gasto por Categoria**: gráfico de barras comparando quanto se gastou em cada categoria no período filtrado, acessível de dentro da própria tela de Categorias. Dois modos alternáveis por chip: **"Por Compra"** (padrão) soma o valor de cada compra pela categoria dela — cobre compras Variáveis, Fixas, Parceladas e Rápidas igualmente; **"Por Produto"** é o gráfico de sempre, item a item (só compras Variáveis). Em ambos, compras/produtos sem categoria entram como "Sem categoria".

## 🏪 Estabelecimentos
- Lista de estabelecimentos ranqueada por valor gasto no período filtrado (estabelecimentos sem compra no período aparecem com R$ 0,00, não somem da lista), com busca por nome (ou apelido).
- FAB **"+"** para cadastrar um estabelecimento diretamente (nome obrigatório; CNPJ, endereço, apelido e categoria opcionais) — reaproveita o cadastro já existente por trás do fluxo de compra manual.
- **Apelido personalizado** (editável por um ícone de lápis no resumo do estabelecimento): qualquer estabelecimento pode receber um apelido (ex. "Mercado da esquina" em vez da razão social completa). Quando definido, o apelido passa a aparecer em **todo o app** (Registros, detalhe de compra, histórico de produto), com o nome real exibido pequeno embaixo.
- **Categoria do estabelecimento** (linha "Categoria: X" clicável no resumo, abre o mesmo seletor usado em Produtos): usada para preencher automaticamente a categoria das compras Variáveis feitas ali (ver "Cadastro manual de compra" abaixo). Ao definir ou trocar a categoria de um estabelecimento, todas as compras Variáveis já feitas ali que ainda estavam "Sem categoria" são atualizadas de uma vez — compras que já tinham categoria própria não são sobrescritas.
- Estabelecimentos sem CNPJ (cadastrados manualmente) podem ser **vinculados** depois a um estabelecimento com CNPJ (opção só aparece no menu de opções — ícone de engrenagem no topo — quando o estabelecimento não tem CNPJ), fundindo o histórico — irreversível, com confirmação. Se os dois tiverem categorias diferentes, o app pergunta antes qual deve prevalecer.
- **Detalhe do estabelecimento**: resumo (CNPJ, endereço, categoria, total gasto), gráfico de gasto mês a mês, e lista das compras feitas ali (com filtro de período) — cada uma clicável para o detalhe completo da compra.

## ⚖️ Saldo e Balanço
- O card "Saldo" na Home resume o que sobrou (ou faltou) no período: Renda total menos Despesas totais (compras normais + fixas + parceladas).
- **Balanço** (tocando no card Saldo): gráfico dos últimos 12 meses com barras de Renda e Despesas lado a lado por mês, mais uma linha marcando o Saldo de cada mês — janela fixa, não respeita o filtro de período de nenhuma outra tela. Abaixo do gráfico, uma lista mês a mês com os três valores.

## 📥 A Receber
- Em Configurações → Renda → **A Receber**: valores que ainda vão entrar (geralmente de outras pessoas), diferente de um lançamento de Renda que já é dinheiro recebido de fato.
- Cadastro parecido com o de Renda: descrição, valor, categoria, fonte e uma data prevista para o recebimento — mas com três tipos: **Pontual** (avulso), **Fixa** (repete todo mês automaticamente) ou **Parcelada** (um número fixo de parcelas, cada uma prevista pra um mês, avançando a partir da data cadastrada).
- Itens com a data prevista já passada e ainda não pagos ficam **destacados em vermelho** na lista e continuam sinalizados até serem pagos ou excluídos — não desaparecem sozinhos.
- **Marcar como pago** transforma aquele item num lançamento de Renda de verdade. Se o pagamento cair num mês diferente do previsto, o app pergunta se quer contar como renda do mês atual ou do mês originalmente previsto.
- Excluir um "A Receber" que já foi pago também exclui a Renda gerada por ele (com aviso antes). Excluir um Fixo ou Parcelado remove aquela ocorrência e todas as futuras já geradas da série (mesmo comportamento das despesas fixas/parceladas).

## ⚙️ Configurações
- Reorganizada em **três seções recolhíveis** (toque no título pra abrir/fechar, todas começam fechadas): **Despesas** (Produtos, Categorias de Produtos, Estabelecimentos, Compras Parceladas), **Renda** (Fontes, Categorias de Renda, A Receber) e **Dados** (Exportar dados).

## 📊 Análises
- **Comparação de Gastos**: gráfico de barras com o total gasto nos últimos 6 meses.
- **Gasto por Categoria** e **gasto mês a mês por estabelecimento**: mesmo componente de gráfico reaproveitado (título + lista + carregando), garantindo visual consistente entre as três análises.

## 🎨 Personalização e acessibilidade
- **Tema preto e dourado** fixo em todo o app (sem alternância claro/escuro).
- **Dois idiomas**: português (padrão) e inglês — todo texto da interface está traduzido.
- **Toda informação cadastrada entra com a primeira letra maiúscula automaticamente** — nome de estabelecimento, produto, categoria, fonte de renda, descrições — não precisa se preocupar em digitar maiúscula.
- **Filtros flutuantes e minimizáveis**: em qualquer tela com filtro, um chip "Filtros" abre um painel sobre o conteúdo em vez de empurrar a lista para baixo.
- **Filtro por período num calendário só**: escolher a data inicial e final acontece no mesmo calendário (antes eram dois calendários separados), e duas setinhas do lado deixam avançar ou voltar o período inteiro de uma vez (ex. se o período é de uma semana, a seta pula pra semana anterior/seguinte).
- **A Home é quem manda no período pro resto do app**: o período escolhido na Home é o ponto de partida de toda outra tela com filtro (Registros, Análise, Todos os Produtos, Estabelecimentos, Renda, Gasto por Categoria, Detalhe de Estabelecimento). Só a Home lembra o período escolhido entre sessões do app — nas demais telas, mudar o filtro vale só enquanto a tela estiver aberta; ao reabrir, volta a copiar o período atual da Home. Produtos Cadastrados e A Receber não têm filtro de período.
- Botão de voltar consistente em todas as telas secundárias; as abas da bottom bar não têm seta de voltar (são destinos principais).

## 💾 Exportação e importação de dados (backup)
- Em Configurações → Dados, botão **"Exportar dados (JSON)"** gera um backup completo (estabelecimentos, produtos, categorias, compras e itens, categorias de renda, fontes e lançamentos de renda) e abre o menu nativo de compartilhamento do Android (e-mail, Drive, WhatsApp etc.). Nome do arquivo com data (`shopcontrol_backup_2026-08-28.json`).
- Botão **"Importar dados (JSON)"**, ao lado: abre o seletor de arquivos do Android, pede confirmação (a ação não pode ser desfeita) e adiciona os dados do arquivo ao app — estabelecimentos, categorias e produtos já existentes com o mesmo nome/CNPJ são reaproveitados, não duplicados. Ao final, mostra quantos registros de cada tipo foram importados.

## ⚙️ Robustez dos dados
- Banco local (Room/SQLite), na versão 5 do schema, com **migrações reais** (nunca apaga dados ao atualizar o app) — inclusive uma migração que corrigiu retroativamente estabelecimentos que haviam sido duplicados por um bug já corrigido.

---

## Ainda não implementado

Todas as 8 features da primeira rodada de planejamento e as 4 features da segunda rodada (`ShopControl_Planejamento.md`) já foram implementadas, e a importação de backup JSON (item do backlog) também. Restam só ideias de backlog, ainda sem detalhamento de tela/decisões fechadas:
- Orçamento mensal com alerta (parcialmente coberto pelo Saldo/Balanço — falta decidir onde a meta é cadastrada e como o alerta dispara).
- Sugestão automática de categoria / categorização em lote.
- Reserva / meta de poupança.
