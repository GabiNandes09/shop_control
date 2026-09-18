# ShopControl — Documento de Planejamento

> Complementa o `Shop_control_contex.md` (estado atual do app, funcionalidades já implementadas) e o `CLAUDE.md` (arquitetura técnica). Reúne o planejamento de features futuras, discutidas em conversa e editadas incrementalmente conforme as decisões avançam. Um documento único, dividido em uma seção por feature. Features já implementadas saem deste arquivo e ficam documentadas no `Shop_control_contex.md`/`CLAUDE.md` — a numeração abaixo reinicia em 1 a cada leva de features implementada.
>
> Nesta atualização: as Features 1–2 da leva anterior (Categoria por Estabelecimento, Divisão de Conta) já foram implementadas e saíram deste documento — ver `Shop_control_contex.md`/`CLAUDE.md`. Nenhuma feature nova entrou ainda nesta leva.

---

## Backlog geral (pendências ainda não planejadas em detalhe)

- **Orçamento mensal com alerta** — parcialmente coberto pelas features já implementadas: com Renda, Despesas Fixas/Parceladas/Rápidas e Saldo/Balanço prontos, "orçamento com alerta" vira essencialmente comparar o Saldo/gasto do período contra uma meta definida pelo usuário. Falta detalhar essa parte (onde a meta é cadastrada, como o alerta é disparado).
- Sugestão automática de categoria / categorização em lote.
- **Reserva / meta de poupança** (ex. planilha tem uma linha fixa "guardar R$X até tal mês") — mencionada na comparação com a planilha, sem detalhamento ainda; menor prioridade.
- **Backup/importação JSON ainda não cobre `ARecebimentoEntity` nem `DivisaoContaEntity`** — gap pré-existente (A Receber já não era coberto antes da Divisão de Conta); sem decisão fechada sobre prioridade.
- **Edição de divisão de conta já salva** — hoje a divisão só pode ser configurada na criação da compra (decisão deliberada de escopo da implementação anterior). Se algum dia for necessário editar participantes/valores depois, e propagar isso pros A Receber já gerados (inclusive só os ainda não pagos), precisa de planejamento próprio.
