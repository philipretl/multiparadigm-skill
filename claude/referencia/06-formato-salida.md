# 06 — Mandatory output format (artifacts on disk)

> The output of `/multiparadigm` is **not console text**: it is a **versioned directory** of
> artifacts, OpenSpec style. The console prints **only** a brief summary + the directory
> **path**. This leaves an auditable record of *what was analyzed, what was found, which
> diagrams, and what is planned*. The diffs and the "after" code are written to files, but
> **not applied to the working tree until the human approves** (see `agent.md` §4).

---

## Output directory

Create at the **repo root**:

```
multiparadigm-<YYYY-MM-DD>/        (if it already exists, add suffix -2, -3, …)
```

It stays **tracked by git** (do not add it to `.gitignore`). Do **not** commit unless the
user asks for it (hard rule #11).

### Structure

```
multiparadigm-<date>/
├── README.md              # Index / audit entry: scope · status · links · summary
├── 1-deteccion.md         # Phase 1: balance + subdomains + embedded baseline diagrams
├── 2-patrones.md          # Phase 2 (table)
├── 3-reglas-negocio.md    # Phase 3 (table)
├── 4-aplicacion.md        # Phase 4 (table: finding·criterion·mechanism·stereotype·mode)
├── 5-refactorizacion.md   # Phase 5: conceptual diff + "after" code + absorbed changes
├── 6-delta-flexibilidad.md# Phase 6: tables per metric + big-O verdict
├── 7-metricas-proceso.md  # Phase 7: agentic process metrics (RQ-C4)
├── 8-bibliografia.md      # Phase 8: anchors used
├── tasks.md               # "What is planned": proposals · mode · criterion · status
└── diagrams/
    └── <module>/
        ├── baseline-clases.mmd      # Mermaid source (neutral standard UML)
        ├── baseline-clases.svg      # rendered image
        ├── baseline-secuencia.mmd   # if the subdomain warrants it
        ├── baseline-secuencia.svg
        ├── after-clases.mmd         # after approved Phase 5 (with the technique's profile)
        └── after-clases.svg
```

One `diagrams/<module>/` subdirectory per analyzed module/subdomain.

---

## Diagrams: `.mmd` source + rendered `.svg` image

Each diagram is emitted as **two** files: the Mermaid source `.mmd` and its **rendered SVG
image**. The **baseline** snapshot goes in neutral standard UML (without the technique's
profile, see `02-perfil-uml.md`); the **"after"** diagram does carry the profile.

1. Write the `.mmd`.
2. Render it to SVG with **mermaid-cli**:
   ```bash
   npx -y @mermaid-js/mermaid-cli -i diagrams/<module>/baseline-clases.mmd \
       -o diagrams/<module>/baseline-clases.svg
   ```
   or run the helper `scripts/render-mmd.sh <multiparadigm-dir>` (renders **all** the `.mmd`).
3. If mermaid-cli is **not available** (no network / no Chromium), leave the `.mmd`, **do not
   fail**, and record the pending render in `README.md` with the exact command. **Never** mark
   a diagram as rendered if the `.svg` does not exist.

The `.md` files **embed** the image and link the source:

```markdown
![Classes — <module> (before)](diagrams/<module>/baseline-clases.svg)
Source: [`baseline-clases.mmd`](diagrams/<module>/baseline-clases.mmd)
```

---

## Templates per file

### `README.md` — index / audit entry
```markdown
# Multiparadigm — <date>
> Scope: <modules/paths> · Language: <…> · Status: analysis | proposed | applied · /multiparadigm

## What was analyzed
- <module> — <subdomain> — [detection](1-deteccion.md)

## What was found (summary)
- Patterns: <n> · Business rules: <n> · Proposals: <n> (<autonomous>/<guided>)

## Diagrams (baseline)
- <module> — ![classes](diagrams/<module>/baseline-clases.svg)

## What is planned
See [tasks.md](tasks.md). <Pending renders, if any, with their command.>

## Index
1. [Detection](1-deteccion.md) · 2. [Patterns](2-patrones.md) · 3. [Rules](3-reglas-negocio.md)
4. [Application](4-aplicacion.md) · 5. [Refactoring](5-refactorizacion.md)
6. [Delta](6-delta-flexibilidad.md) · 7. [Metrics](7-metricas-proceso.md) · 8. [Bibliography](8-bibliografia.md)
```

### `tasks.md` — what is planned
```markdown
# Proposals and status

| # | Proposal | Module · file | Mode | Criterion (Table 4.1) | Status |
|---|---|---|---|---|---|
| P1 | <short description> | `<file>` | autonomous/guided | <…> | ⏳ pending · ✅ approved · 🚀 applied · ❌ rejected |

- [ ] **P1** — <what it does> — applies to `<file>`
```

### `1-deteccion.md` … `8-bibliografia.md`
The content of sections 1–8 of the technique, **one phase per file**. Section 1 embeds the
baseline diagrams (`.svg` images). Structure of each section:

```markdown
## 1. Detection
- Language and version · Framework / runtime
- OO signals · FP signals · Paradigm balance <0.0–1.0> (<interpretation>)
- Subdomain(s) and governing paradigm (M6)

### 1.1 Baseline diagram(s) — "before" state (mandatory)
**Neutral standard** UML, reverse engineering **without the technique's notation** (no
`<<pure>>`, `<<ValueObject>>`, `{immutable}`, R* rules, M* mechanisms, criteria, or big-O:
that biases the comparison and belongs to the "after"). Only classes, `<<interface>>`,
attributes, operations, and relationships. One diagram per independent subdomain. Embed each
`.svg` and link its `.mmd`.

![Classes — <module> (before)](diagrams/<module>/baseline-clases.svg)
```

```markdown
## 2. Detected patterns
| # | Pattern (catalog) | Location | Current form | Tension (Expression Problem, etc.) | Mech. |
|---|---|---|---|---|---|
```

```markdown
## 3. Business rules
| # | Rule (natural language) | Location | Mixing with mechanics | Suggested paradigm | Why |
|---|---|---|---|---|---|
```

```markdown
## 4. Technique application
| Finding | Criterion (Table 4.1) | Mechanism (M1–M6) | UML stereotype(s) | Mode (autonomous/guided) |
|---|---|---|---|---|
```

```markdown
## 5. Refactoring proposal
### 5.1 Conceptual diff  (before vs. after, module by module, brief bullets)
### 5.2 "After" code  (with the profile stereotypes as comments)
### 5.3 "After" diagram(s)  (with the technique's profile; link diagrams/<module>/after-*.svg)
### 5.4 Absorbed anticipated changes  (per step: where · files touched · LoC modified vs added)
```

```markdown
## 6. Flexibility delta
### 6.1 Declared evolution steps (fixed BEFORE measuring — Eden & Mens)
### 6.2 Per metric (one table per metric: C¹_Classes, C^LoC, C^CC, C^Add/LoC, t(δ))
| Step | Dimension | Baseline | Multiparadigm | Δ | Big-O verdict |
|---|---|---|---|---|---|
### 6.3 Conclusion per dimension (extensibility · modularity · variation points)
### 6.4 Verdict (does any step go from O(|N|) to O(1) without degrading the others?)
```

```markdown
## 7. Agentic process metrics
- Proposals per phase · Autonomous vs guided decisions · Acceptance rate · Human interventions
```

```markdown
## 8. Bibliographic anchors used
<Eden & Mens 2006 · Kallel 2018 · Coplien 2000 · Narbel 2007 · Oliveira & Cook 2012 · …>
```

---

## Rule

**Do not omit files.** If a section does not apply, create it anyway with "N/A — reason". The
directory must be self-contained and auditable without rereading the console conversation.
