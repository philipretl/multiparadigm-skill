# multiparadigm-skill

> 🌐 **Interactive page / Página interactiva de la técnica**: EN https://philipretl.github.io/multiparadigm-skill/ · ES https://philipretl.github.io/multiparadigm-skill/es.html

> Portable skill that exposes the `/multiparadigm` command in Claude Code / Cowork, GitHub Copilot, Grok, ChatGPT, Cursor, and any assistant that accepts a system prompt.

`/multiparadigm` analyzes code under the **multiparadigm functional + object-oriented approach** and returns a structured report with:

1. Detection of OO/FP balance and subdomain.
2. Patterns from the catalog (16) detected in the code.
3. Implicit business rules, with the suggested paradigm to encapsulate them.
4. Technique application: assignment criterion + abstraction mechanism + refactoring category.
5. Concrete refactoring proposal (with after code and absorbed anticipated changes).
6. **Flexibility delta** before/after along three dimensions: extensibility, modularity, variation points. Plus an aggregate Flex-Score.

## Origin

This skill operationalizes the technique from the undergraduate research project *"Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach"* (Andres Felipe Vega Noguera, Yeison David Mosquera Navarro; advisor PhD. Julio Ariel Hurtado Alegria; **Universidad del Cauca**, Faculty of Electronic Engineering and Telecommunications, IDIS Research Group, 2026).

It does not introduce invented metrics: the entire apparatus is anchored in the thesis's QA > 2.0 corpus (68 effective studies) and the canonical references (Eden & Mens 2006; Kallel et al. 2018; Heinzl & Schreibmann 2018; Heithoff 2023; Coplien 2000; Vranic 2009; Narbel 2007; Castro 2020; Oliveira & Cook 2012).

## Repo structure

```
multiparadigm-skill/
├── README.md                    # this file
├── shared/                      # source of truth
│   ├── MODEL.md                 # vocabulary, metrics, catalog (DO NOT REWRITE IN ADAPTERS)
│   └── PROMPT.md                # canonical prompt with 6 phases and mandatory Markdown output
├── claude/                      # Claude Code / Cowork adapter
│   ├── SKILL.md
│   ├── plugin.json
│   └── multiparadigm.command.md
├── copilot/                     # GitHub Copilot adapter
│   ├── README.md
│   └── copilot-instructions.md
├── .github/prompts/             # prompt files for Copilot Chat
│   └── multiparadigm.prompt.md
├── grok-chatgpt/                # Grok / ChatGPT / Claude.ai adapter
│   ├── system-prompt.md
│   └── user-template.md
├── cursor/                      # Cursor adapter
│   ├── .cursorrules
│   └── multiparadigm.mdc
└── examples/
    └── before-after/            # end-to-end case (Java Visitor interpreter → Java 21 Object Algebra)
        ├── input.java
        ├── after.java
        └── REPORT.md
```

## Installation per tool

### Claude Code / Cowork

Package `claude/` + `shared/` + `examples/` as a `.plugin` and load it:

```bash
zip -r multiparadigm-skill.plugin claude shared examples
# then in Claude Code:
# /plugin install ./multiparadigm-skill.plugin
```

Invoke:

```
/multiparadigm
```

or pasting the code after the command.

### GitHub Copilot

1. Copy `.github/prompts/multiparadigm.prompt.md` to the target repo.
2. Copy the contents of `copilot/copilot-instructions.md` into `.github/copilot-instructions.md` of the target repo (or concatenate it onto the existing one).
3. Make sure `shared/MODEL.md` and `shared/PROMPT.md` are accessible in the repo (commit directly or use a submodule).
4. In Copilot Chat: `/multiparadigm` (with code selected or pasted).

### Grok / ChatGPT / Claude.ai (without Cowork)

1. Paste `grok-chatgpt/system-prompt.md` as a **system message** (in ChatGPT: Custom GPT instructions or Project instructions; in Grok: system prompt; in Claude.ai: project instructions).
2. Invoke with the `grok-chatgpt/user-template.md` template.

### Cursor

1. Copy `cursor/.cursorrules` to the root of the target repo.
2. Copy `cursor/multiparadigm.mdc` to `.cursor/rules/` of the target repo.
3. Invoke in Cursor chat: `/multiparadigm` with the file open or code selected.

## What the command returns

A Markdown document with this structure (non-negotiable, comes from `shared/PROMPT.md` §OUTPUT):

```
# Multiparadigm report — `<file or module>`
## 1. Detection
## 2. Detected patterns
## 3. Business rules
## 4. Technique application
## 5. Refactoring proposal
   ### 5.1 Conceptual diff
   ### 5.2 After code
   ### 5.3 Absorbed anticipated changes
## 6. Flexibility delta
   ### 6.1 Per dimension (table)
   ### 6.2 Flex-Score (Before / After / Delta)
   ### 6.3 Verdict
## 7. Bibliographic anchors used
```

## Flexibility model (from the thesis)

**Three dimensions** (`shared/MODEL.md` §1):

- **Extensibility** (Eden & Mens 2006) — LOC modified vs added vs preserved over the 3 standard anticipated changes.
- **Modularity** (Kallel et al. 2018) — Ca/Ce/I, LCOM4, DIT, syntactic cohesion.
- **Variation points** (Heinzl & Schreibmann 2018; Heithoff 2023) — polymorphic instances pluggable without touching the core, wiring cost.

**Flex-Score** = 0.40 × Extensibility + 0.35 × Modularity + 0.25 × VariationPoints (range 0.0-1.0).

Weights derived from mention frequency in the thesis's QA > 2.0 corpus.

## Hard rules of the command

- Does not invent metrics outside `shared/MODEL.md`.
- Does not deliver OO-vs-FP verdicts. Follows Coplien (2000): paradigm is chosen by domain.
- Reports honest deltas. If Flex-Score drops, it says so.
- Castro (2020) [#30 of the corpus]: paradigm alone does not determine quality.
- If the code is trivial or already optimal, marks category "Not applicable".
- Default behavior is **same-language refactoring**; switches target only when the user asks for `--target <lang>` or the source language genuinely lacks the mechanism.

## Example

See [`examples/before-after/`](./examples/before-after/) for a complete case: Visitor interpreter in Java refactored to Java 21 with sealed interfaces + records + Object Algebra (M1) + ADT/pattern matching (M3), with full report and flexibility calculations.

## License

MIT.

## Academic citation

If you use this skill in research, please cite:

> Vega Noguera, A. F.; Mosquera Navarro, Y. D.; Hurtado Alegria, J. A. (2026). *Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach*. Undergraduate research project, Systems Engineering Program, Faculty of Electronic Engineering and Telecommunications, Universidad del Cauca, Popayan, Colombia.

Project annex on Parsifal: <https://parsif.al/philipretl/tecnica-para-el-diseno-de-software-flexible-siguiendo-un-enfoque-multi-paradigma-funcionaloo/>
