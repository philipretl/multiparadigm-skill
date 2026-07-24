# multiparadigm-skill

> Interactive page of the technique: EN <https://philipretl.github.io/multiparadigm-skill/> · ES <https://philipretl.github.io/multiparadigm-skill/es.html>

Provider-agnostic skill that exposes the `/multiparadigm` command in AI coding assistants: Claude Code / Cowork, GitHub Copilot, Cursor, Grok, ChatGPT / OpenAI, and any assistant that accepts a system prompt.

`/multiparadigm` analyzes code under the **multiparadigm functional + object-oriented approach** and produces a structured report with:

1. Detection of the OO/FP balance and the subdomain.
2. Catalog patterns (16) detected in the code.
3. Implicit business rules, with the suggested paradigm to encapsulate them.
4. Technique application: assignment criterion + abstraction mechanism + UML stereotypes.
5. Concrete refactoring proposal ("after" code and absorbed anticipated changes), applied only under human approval.
6. **Flexibility delta** before/after along three dimensions: extensibility, modularity, variation points.

The skill is written in English, but it always **responds in the language the user writes in**.

## Design philosophy: provider-agnostic

The technique is defined **once** and adapted **per provider**. A shared, provider-neutral core holds the conceptual model and the canonical prompt; each top-level folder is an **adapter** that packages the command for one AI provider, in that provider's native format (skill/plugin, instructions file, rules file, or plain system prompt). Nothing in the technique depends on a specific vendor: any assistant that can read Markdown instructions can run it.

| Adapter | Provider | Format |
|---|---|---|
| `claude/` | Claude Code / Cowork | Skill (`SKILL.md` + `agent.md` + `plugin.json`) — **current version** |
| `copilot/` | GitHub Copilot | `copilot-instructions.md` + prompt file |
| `cursor/` | Cursor | `.cursorrules` + `multiparadigm.mdc` |
| `grok-chatgpt/` | Grok / ChatGPT / Claude.ai | System prompt + user template |
| `shared/` | Any (provider-neutral core) | `MODEL.md` + `PROMPT.md` |

The `claude/` adapter is the current version, aligned with Chapters 4–5 of the monograph: it works as an agent (six phases, three decision modes, approval-gated application, artifacts on disk) and measures flexibility exclusively with the Eden & Mens evolution-cost model. The `shared/`, `copilot/`, `cursor/`, and `grok-chatgpt/` adapters are the previous generation of the command (report-only, with the legacy Flex-Score aggregate), kept as a working historical reference.

## Origin

This skill operationalizes the technique from the undergraduate research project *"Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach"* (Andres Felipe Vega Noguera, Yeison David Mosquera Navarro; advisor PhD. Julio Ariel Hurtado Alegria; **Universidad del Cauca**, Faculty of Electronic Engineering and Telecommunications, IDIS Research Group, 2026).

It does not introduce invented metrics: the entire apparatus is anchored in the thesis's QA > 2.0 corpus (68 effective studies) and the canonical references (Eden & Mens 2006; Kallel et al. 2018; Heinzl & Schreibmann 2018; Heithoff 2023; Coplien 2000; Vranic 2009; Narbel 2007; Castro 2020; Oliveira & Cook 2012).

## Repository structure

```
multiparadigm-skill/
├── README.md                      # this file
├── shared/                        # provider-neutral core (legacy version)
│   ├── MODEL.md                   # vocabulary, metrics, catalog (do not rewrite in adapters)
│   └── PROMPT.md                  # canonical prompt: 6 phases + mandatory Markdown output
├── claude/                        # Claude Code / Cowork adapter (current version)
│   ├── SKILL.md                   # skill entry point for /multiparadigm
│   ├── agent.md                   # orchestration: phases, decision modes, approval flow
│   ├── plugin.json
│   ├── reference/                 # conceptual source of truth (Chapter 4)
│   │   ├── 01-decision-criteria.md
│   │   ├── 02-uml-profile.md
│   │   ├── 03-pattern-catalog.md
│   │   ├── 04-evaluation-model.md
│   │   ├── 05-application-guide.md
│   │   └── 06-output-format.md
│   └── scripts/render-mmd.sh      # renders .mmd diagrams to .svg (mermaid-cli)
├── copilot/                       # GitHub Copilot adapter
│   ├── README.md
│   └── copilot-instructions.md
├── cursor/                        # Cursor adapter
│   ├── .cursorrules
│   └── multiparadigm.mdc
├── grok-chatgpt/                  # Grok / ChatGPT / Claude.ai adapter
│   ├── system-prompt.md
│   └── user-template.md
├── docs/                          # GitHub Pages: interactive page (EN/ES) + reference PDFs
└── examples/
    └── before-after/              # end-to-end case (Java Visitor → Java 21 Object Algebra)
        ├── input.java
        ├── after.java
        └── REPORT.md
```

## Installation per provider

### Claude Code / Cowork (current version)

Package `claude/` + `shared/` + `examples/` as a plugin and load it:

```bash
zip -r multiparadigm-skill.plugin claude shared examples
# then in Claude Code:
# /plugin install ./multiparadigm-skill.plugin
```

Invoke with `/multiparadigm` (optionally with a path, `--domain`, `--deltas`, `--dry-run`, `--no-render`, or `--target <lang>`; see `claude/SKILL.md`). The output is a versioned artifacts directory `multiparadigm-<date>/` at the repo root (per-phase reports, `tasks.md`, and UML diagrams as `.mmd` + rendered `.svg`), per `claude/reference/06-output-format.md`.

### GitHub Copilot

1. Copy the contents of `copilot/copilot-instructions.md` into `.github/copilot-instructions.md` of the target repo (or concatenate it onto the existing one).
2. Make sure `shared/MODEL.md` and `shared/PROMPT.md` are accessible in the repo (commit them directly or use a submodule).
3. In Copilot Chat: `/multiparadigm` (with code selected or pasted). See `copilot/README.md`.

### Grok / ChatGPT / Claude.ai (without Cowork)

1. Paste `grok-chatgpt/system-prompt.md` as a **system message** (in ChatGPT: Custom GPT or Project instructions; in Grok: system prompt; in Claude.ai: project instructions).
2. Invoke with the `grok-chatgpt/user-template.md` template.

### Cursor

1. Copy `cursor/.cursorrules` to the root of the target repo.
2. Copy `cursor/multiparadigm.mdc` to `.cursor/rules/` of the target repo.
3. Invoke in Cursor chat: `/multiparadigm` with the file open or code selected.

## Hard rules of the command

- Does not invent metrics outside the reference model (`claude/reference/04-evaluation-model.md`; legacy adapters: `shared/MODEL.md`).
- Does not deliver OO-vs-FP verdicts. Follows Coplien (2000): the domain chooses the paradigm.
- Reports honest deltas: if flexibility drops, it says so.
- If the code is trivial or already optimal, marks the category "Not applicable".
- Default behavior is same-language refactoring; switches target only with `--target <lang>` or when the source language genuinely lacks the mechanism.
- Responds in the user's language, regardless of the skill being written in English.
- In the `claude/` adapter, no change is applied without explicit human approval (universal audit).

## Example

See [`examples/before-after/`](./examples/before-after/) for a complete case: a Visitor interpreter in Java refactored to Java 21 with sealed interfaces + records + Object Algebra (M1) + ADT/pattern matching (M3), with the full report and flexibility calculations.

## License

MIT.

## Academic citation

If you use this skill in research, please cite:

> Vega Noguera, A. F.; Mosquera Navarro, Y. D.; Hurtado Alegria, J. A. (2026). *Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach*. Undergraduate research project, Systems Engineering Program, Faculty of Electronic Engineering and Telecommunications, Universidad del Cauca, Popayan, Colombia.

Project annex on Parsifal: <https://parsif.al/philipretl/tecnica-para-el-diseno-de-software-flexible-siguiendo-un-enfoque-multi-paradigma-funcionaloo/>
