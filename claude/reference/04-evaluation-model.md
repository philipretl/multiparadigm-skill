# 04 — Flexibility evaluation model (§4.5)

> Source of truth for **Phase 6**. The model is adopted from the **evolution complexity**
> framework of Eden & Mens (2006) and complemented with the paradigm-independent
> architectural constraints of Kallel et al. (2018).
>
> **Do not invent metrics.** Only the ones in this document. In particular, do **not** use
> ad hoc weighted aggregates ("Flex-Score" style): they are not part of the technique.

---

## Core idea

Flexibility is **not absolute**: it is relative to a **class of changes declared
upfront**. That class is called an **evolution step** and is formalized as a four-element
tuple:

> (original problem, modified problem, implementation satisfying the original,
> implementation satisfying the modified one).

Measuring flexibility reduces to measuring the **cost of transforming** the first
implementation into the second: how many modules change, weighting each module by a
complexity metric.

**Generic form of the evolution cost:**

```
C^μ_Modules(δ) = Σ_m μ(m)
```

where the sum runs over the modules `m` in the **symmetric difference** between the previous
implementation and the adjusted one that solves step `δ`, and `μ` is a complexity metric (LoC,
cyclomatic complexity, etc.). The granularity of *Modules* is fixed to **class, function, or
package** as convenient.

---

## Table 4.4 — Battery of evolution-cost metrics

| Metric | Definition | Instrumentation |
|---|---|---|
| `C¹_Classes(δ)` | Number of classes added, removed, or modified by `δ` (uniform weight) | Diff between commits of the previous and adjusted implementations; count of changed files |
| `C^LoC_Modules(δ)` | Lines of code touched by `δ`, summed over the modified modules | `diff` statistics (insertions and deletions) per file (cloc) |
| `C^CC_Modules(δ)` | Sum of the cyclomatic complexity (McCabe) of the modules touched by `δ` | Static analysis for the target language (Lizard, SonarQube, or equivalent) |
| `C^Add/LoC_Modules(δ)` | LoC **only** of the new modules created by `δ` | Diff filtered to created files, weighted by the module's LoC |
| `t(δ)` | Timed duration of the developer executing step `δ` | Stopwatch; median over repetitions when applicable |

---

## Asymptotic interpretation (big-O notation)

The values are interpreted in terms of asymptotic complexity:

- **Easy step** -> its cost **does not grow** with the size of the implementation: constant `O(1)`.
- **Hard step** -> grows linearly `O(|N|)` with respect to a relevant subset `N` (e.g.
  the number of domain data types or the number of existing operations).

> The multiparadigm technique is considered **advantageous** when, over the steps declared
> upfront, it **shifts steps from `O(|N|)` to `O(1)`** without degrading the cost of the other
> steps.

**The agent explicitly reports** when the same step goes from linear to constant complexity
between the baseline and the multiparadigm design (e.g. when adding an operation no longer
requires modifying every document type).

---

## Dimensions under evaluation

Each dimension has its associated **class of evolution steps**:

| Dimension | Class of evolution steps |
|---|---|
| **Extensibility** | Steps that introduce a domain type or an operation over the existing types |
| **Modularity** | Steps that redefine an architectural boundary or redistribute responsibilities across modules |
| **Variation points** (configurability) | Steps that replace an algorithm or introduce a focused criterion |

> Thesis note: flexibility is named explicitly in only 16 of the 68 corpus studies,
> versus modularity (47), extensibility (42), and reusability (25). That is why it is measured
> through these three concrete, verifiable dimensions, not as an abstract construct.

---

## Procedure (mandatory upfront declaration)

1. **Declare before measuring** the set of evolution steps (Eden & Mens: flexibility
   is only comparable if the class of changes is fixed beforehand). Choose the steps to
   cover the four dimensions of Table 4.1.
2. Obtain the **two versions** to compare: the **baseline** (frozen starting design) and the
   **multiparadigm design**. Both share language, test suite, and observable behavior.
3. **Apply each step to each version** and record, per step × version combination, the
   Table 4.4 metrics and the implementation time.
4. Report a **table per metric** (steps as rows, versions as columns) with the relative
   difference against the baseline, and annotate in big-O the steps that change complexity
   class.

**Standard ecosystem instrumentation:** `cloc` and static analysis for the target language
(e.g. TypeScript). Document the catalog -> language-construct mapping when there are
non-trivial equivalents (discriminated unions for ADTs, interfaces + generics for type
classes, explicit encodings for object algebras).

---

## Example report table (Phase 6)

**Metric `C¹_Classes(δ)` — classes touched per step:**

| Step | Description | Dimension | Baseline | Multiparadigm | Δ | Verdict |
|---|---|---|---|---|---|---|
| δ₁ | New document type | Extensibility by types | `O(1)` (n classes) | `O(1)` | = | OO favored |
| δ₂ | New operation over existing types | Extensibility by operations | `O(|N|)` | `O(1)` | ↓ | **FP: O(|N|)->O(1)** |
| … | … | … | … | … | … | … |

Repeat for `C^LoC`, `C^CC`, `C^Add/LoC`, and `t(δ)`. Conclude per dimension and globally,
always with honest deltas.
