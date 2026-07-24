---
name: multiparadigm
description: Analyzes and refactors code under the FP/OO multiparadigm approach of the Vega-Mosquera-Hurtado technique (Universidad del Cauca, 2026). Detects patterns, separates business rules, proposes (and, upon approval, applies) a refactoring, and measures the before/after flexibility delta in extensibility, modularity, and variation points with the Eden & Mens model. Use when the user types `/multiparadigm`, asks for a "multiparadigm analysis", "multiparadigm refactoring", "how would this code's flexibility improve", "hybridize OO and FP", or hands over a pure OO or FP fragment to hybridize it.
allowed-tools: Read, Grep, Glob, Edit, Write, Bash
---

# Skill: /multiparadigm

This skill encodes the *FP/OO Multiparadigm Design Technique* (Vega Noguera, Mosquera Navarro;
advisor PhD. Julio Ariel Hurtado Alegria; Universidad del Cauca, 2026) as an agent that applies
the technique to the current repository.

> **Language:** this skill is written in English, but always respond in the language the user
> writes in, regardless of this skill being written in English.

## Procedure

1. **Read `agent.md`** (in this same folder). It is the entry point: it orchestrates the six
   phases, the three decision modes, and the approval-gated application flow.
2. **Read, on demand, the `reference/` documents** each phase needs (they are the conceptual
   source of truth; do not invent criteria or metrics outside of them).
3. **Run the six phases in order** (Detection -> Patterns -> Business rules -> Technique
   application -> Refactoring -> Flexibility delta) and **write the output as artifacts** in a
   `multiparadigm-<date>/` directory at the repo root (per-phase report, `tasks.md`, and UML
   diagrams `.mmd`+`.svg` rendered), following `reference/06-output-format.md`. On the
   console, print only a summary + the directory path.
4. **Universal human audit:** no change is applied without explicit approval. Upon approval,
   apply to the working tree and run the existing tests to verify that behavior is preserved.
   Do not commit or push unless the user asks for it.

## Arguments

- `/multiparadigm` — analyze the current repo (limit to `src/` or equivalent).
- `/multiparadigm <path>` — analyze a file or directory.
- `/multiparadigm --domain "<text>"` — provide domain context.
- `/multiparadigm --deltas <file>` — use a declared list of evolution steps.
- `/multiparadigm --dry-run` — write the artifacts directory; do not modify the source code.
- `/multiparadigm --no-render` — generate the `.mmd` diagrams but skip the SVG render (mermaid-cli).
- `/multiparadigm --target <lang>` — propose the refactoring in another multiparadigm language.

If there are no arguments and the repo is ambiguous (unclear language or domain), ask once
before analyzing.
