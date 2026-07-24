# multiparadigm-agent

Agentic instrument of the **FP/OO Multiparadigm Design Technique — Flexibility-Oriented Hybrid
Approach** (Vega Noguera, Mosquera Navarro; advisor: PhD. Julio Ariel Hurtado Alegria;
Universidad del Cauca, 2026). It corresponds to **phase 2** (agentic proof of concept) of the
case study in Chapter 5 of the monograph and answers **RQ-C4**.

## Entry point

> **Read [`agent.md`](agent.md) first.** It is the single entry point of the `/multiparadigm`
> command. It orchestrates the six phases, the three decision modes, and the approval-gated
> application flow.

When the user runs `/multiparadigm` on a repository, the agent:

1. **Detects** language, OO/FP balance, and subdomain.
2. **Reports** the catalog patterns detected.
3. **Reports** the implicit business rules with a suggested paradigm.
4. **Applies** the Table 4.1 criteria and the M1–M6 mechanisms to each finding.
5. **Proposes** a refactoring (diff + absorbed anticipated changes).
6. **Computes** the flexibility delta per dimension (Eden & Mens model).

Every code modification is delivered as a **reviewable proposal** (universal human audit); upon
approval, the agent **applies it to the working tree** and runs the tests to verify that
behavior is preserved. **No change enters without human review.**

## Structure

```
multiparadigm-agent/
├── agent.md                       # ENTRY POINT — command orchestration
├── README.md                      # this file
└── referencia/                    # conceptual source of truth (Chapter 4)
    ├── 01-criterios-decision.md   # Table 4.1 and per-dimension criteria (§4.2)
    ├── 02-perfil-uml.md           # lightweight UML profile + paradigm detectors (§4.3)
    ├── 03-catalogo-patrones.md    # 16 patterns, mechanisms M1–M5, business rules (§4.4)
    ├── 04-modelo-evaluacion.md    # Eden & Mens evolution-cost model (§4.5)
    ├── 05-guia-aplicacion.md      # six-step methodological guide (§4.6)
    └── 06-formato-salida.md       # mandatory Markdown template of the report
```

## Non-negotiable principles

- Paradigm assignment is decided by the **domain**, not by preference (Coplien 2000).
- **Every decision cites a criterion** from Table 4.1 (*criterion -> decision* traceability).
- **Only the Eden & Mens model** to measure flexibility; no invented metrics.
- **Universal human audit**: no change is applied without approval.

## Relation to the legacy skill

The other adapters in this repository (`shared/`, `copilot/`, `cursor/`, `grok-chatgpt/`) are the
**previous version** of the command. They are kept as historical reference. This
`multiparadigm-agent/` (the `claude/` directory) is the current version, aligned with Chapters 4
and 5 of the monograph; in particular, it **discards the legacy aggregate "Flex-Score"**, which
is not part of the technique, and uses exclusively the evolution-cost model of Table 4.4.

## Source

Chapters 4 and 5 of the monograph (`cap4.tex`, `cap5.tex` in the thesis repository).
