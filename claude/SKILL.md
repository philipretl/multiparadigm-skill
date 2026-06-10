---
name: multiparadigm
description: Analyzes code under the OO+FP multiparadigm approach of the Vega-Mosquera-Hurtado technique (Universidad del Cauca, 2026). Detects patterns, separates business rules, proposes a refactoring, and measures flexibility before/after across extensibility, modularity, and variation points. Use when the user requests `/multiparadigm`, "multiparadigm analysis", "multiparadigm refactoring", "how would this code's flexibility improve", "convert this to multiparadigm", or when they hand you a pure OO or FP fragment and ask to hybridize it.
triggers:
  - /multiparadigm
  - multiparadigm analysis
  - multiparadigm refactoring
  - flexibility before and after
  - hybridize OO and FP
allowed-tools: Read, Grep, Glob, Edit, Write
---

# Skill: /multiparadigm

> Multiparadigm OO+FP analysis and refactoring command, based on the undergraduate research project *"Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach"* (Vega Noguera, Mosquera Navarro; advisor PhD. Julio Ariel Hurtado Alegria; Universidad del Cauca, 2026).

## When to use this skill

Activate when the user:
- Types `/multiparadigm` (with or without arguments).
- Asks for an analysis under the OO+FP multiparadigm approach.
- Asks for a refactoring to improve the flexibility of pure OO or pure FP code.
- Hands you a fragment and asks how its extensibility / modularity / variation points would improve.
- Explicitly asks for the flexibility delta before vs after a change.

## Procedure

1. **Read `shared/MODEL.md`** (in the same skill repo, path `../shared/MODEL.md`). It is the source of truth for vocabulary and metrics. Do not invent alternative metrics.
2. **Read `shared/PROMPT.md`**. It is the canonical prompt with the 6 phases (Detection → Patterns → Business rules → Technique application → Refactoring → Flexibility delta) and the mandatory output format.
3. **Apply the canonical prompt** to the code the user provides (file, fragment, repo).
4. **Return the report** in the Markdown format defined in `PROMPT.md` §OUTPUT. Do not omit sections.
5. If the code is trivial or already at its optimum, mark category "Not applicable" instead of forcing a refactoring.

## Rules

- Respond in the user's language (default: neutral technical English).
- Do not deliver OO-vs-FP verdicts; follow Coplien (2000): paradigm is chosen by domain.
- Report honest deltas; if Flex-Score drops, say so.
- Anchor every metric to its bibliographic reference (Eden & Mens 2006; Kallel 2018; Heinzl & Schreibmann 2018; Heithoff 2023).

## Academic anchoring

This skill operationalizes:
- 3 flexibility dimensions (thesis Ch. 5 §Metrics and dimensions).
- 6 FP/OO assignment criteria (thesis Ch. 4 §4.2).
- 6 abstraction mechanisms M1-M6 (thesis Ch. 4 §4.2, derived from QA corpus × Narbel).
- 16 patterns from the catalog (thesis Ch. 4 §4.5).
- 8 refactoring categories (thesis Ch. 4, transversal).
- 6 UML stereotypes (thesis Ch. 4 §4.3).

## Resources

- `../shared/MODEL.md` — conceptual model and metrics (source of truth).
- `../shared/PROMPT.md` — canonical prompt with phases and output.
- `../examples/before-after/` — end-to-end case from input to expected report.
