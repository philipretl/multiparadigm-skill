---
name: Conceptual model for the /multiparadigm command
description: Vocabulary, metrics, and catalog used by the command to analyze and refactor code under the OO+FP multiparadigm approach. Anchored in the Vega-Mosquera-Hurtado thesis (Universidad del Cauca, 2026).
type: shared/source-of-truth
---

# Conceptual model

> This is the **single source of truth** for the `/multiparadigm` command. All adapters (Claude SKILL.md, Copilot prompt, Grok system-prompt, Cursor rules) read from here. Do not restate metrics in each adapter: link to this document.

The model mirrors the conceptual apparatus of the undergraduate research project *"Technique for Designing Flexible Software Following a Multi-Paradigm Functional/OO Approach"* (Vega Noguera, Mosquera Navarro; advisor: PhD. Julio Ariel Hurtado Alegria; Universidad del Cauca, 2026). It does not introduce invented metrics.

---

## 1. What counts as flexibility

The thesis defines code flexibility along three dimensions, each anchored in the state of the art. These are the **only** dimensions the command reports (do not add others without bibliographic justification).

### 1.1 Extensibility

**Definition**: ability to add functionality without altering what already exists.

**Operationalization** (Eden & Mens, 2006 — *"the cost of change as a function of where the change happens"*):

| Metric | How to measure | Rule |
|---|---|---|
| LOC modified | Lines modified in pre-existing files to introduce an anticipated change | Lower is better |
| LOC added | New lines (new files or blocks) for the same change | Neutral; preferable to modified |
| LOC preserved | Lines untouched by the change | Higher is better |
| Anticipated changes applied | Number of "add X" scenarios tested | Minimum 3 (new operation, new datum, new type variant) |

**Standard anticipated changes** (following the thesis case study):
1. Add a **new operation** over existing data (e.g., `Optimizer` over an AST).
2. Add a **new node/datum** over existing operations (e.g., `Match` as a new AST node).
3. Add a **type variant** (e.g., `Bool` and logical operations).

### 1.2 Modularity

**Definition**: degree of independence between parts.

**Operationalization** (Kallel et al., 2018 — architectural constraints as graphs):

| Metric | What it captures | Applies to |
|---|---|---|
| Afferent coupling (Ca) | How many modules depend on this one | OO and FP |
| Efferent coupling (Ce) | How many modules this one depends on | OO and FP |
| Instability I = Ce/(Ca+Ce) | Position on the stable–unstable continuum (Martin) | OO and FP |
| LCOM4 | Class cohesion (connected components in method-field graph) | OO |
| Syntactic cohesion | Reuse of symbols/functions within the module | FP |
| DIT | Depth of inheritance tree | OO |

### 1.3 Configurability / Variation points

**Definition**: ability to adjust behavior without touching source code, materialized as explicit variation points.

> Thesis note (2026-04-29): pure configurability appears in only 1/68 papers of the QA > 2.0 corpus; modularity (47), extensibility (42), and reusability (25) dominate. The dimension is reformulated as **"variation points / extensibility points"** (more modularity-like) without losing the original spirit.

**Operationalization** (Heinzl & Schreibmann, 2018; Heithoff, 2023):

| Metric | How to measure |
|---|---|
| Number of explicit variation points | Typeclass / object algebra / strategy / signal instances that can be plugged in without modifying the core |
| Wiring cost | Lines of configuration needed to enable a new interpretation of the module |
| Open polymorphism | Ability to extend behavior from outside the module (without recompiling the core) |

---

## 2. Flexibility Score (Flex-Score)

> The Flex-Score does **not replace** the dimensions; it is an interpretive aggregate for reporting before/after. Always present it alongside the three dimensions individually.

```
Flex-Score = 0.40 * Extensibility_norm
           + 0.35 * Modularity_norm
           + 0.25 * VariationPoints_norm
```

**Weights**: derived from mention frequency in the thesis's QA > 2.0 corpus (extensibility 42 papers, modularity 47, configurability/variation 1+; reweighted favoring extensibility because it is the most operational dimension for refactoring).

**Normalizations** (range 0.0 – 1.0):

- `Extensibility_norm = 1 - (LOC_modified / LOC_total_change)` averaged over the 3 anticipated changes.
- `Modularity_norm = 1 - (avg_instability + LCOM4_norm) / 2`, where `LCOM4_norm = min(LCOM4 / 5, 1.0)`.
- `VariationPoints_norm = min(variation_points / 5, 1.0)` adjusted by wiring cost (penalize if wiring > 10 LOC).

**Mandatory reporting**: the command shows Flex-Score Before, Flex-Score After, and delta (`Δ = After - Before`) **together with each broken-down dimension**. Do not report only the aggregate.

---

## 3. Paradigm detectors

To report the "paradigm balance" that the refactoring should equalize, the command detects OO and FP proxies in the code.

### 3.1 OO signals

- Classes with mutable state and methods that modify it.
- Inheritance, subtype polymorphism (dynamic dispatch by receiver).
- Interfaces / abstract classes.
- Classic GoF patterns (Visitor, Strategy, Observer, Decorator, Command, Iterator).
- Encapsulation: getters/setters, access modifiers.
- Stable object identity (environments, sessions, connections).

### 3.2 FP signals

- Pure functions with no side effects.
- Higher-order functions (HOF): `map`, `filter`, `reduce`, `flatMap`, callbacks, lambdas.
- Immutability: `const`, `final`, `val`, copies instead of mutation.
- Pattern matching / `match` / destructuring.
- Algebraic types / sealed traits / discriminated unions.
- Function composition, currying, partial application.
- Explicit monads (`Option`, `Result`, `Either`, `Maybe`, `IO`).

### 3.3 Paradigm balance

```
Balance = |OO_signals - FP_signals| / (OO_signals + FP_signals)
```

- `Balance ≈ 0`: balanced code (effective multiparadigm).
- `Balance > 0.7`: mono-paradigm code; candidate for multiparadigm refactoring if the *other* paradigm's signals would serve the domain.

> Balance is not good or bad in itself. Coplien (2000) [#31 of the corpus]: paradigm is chosen by domain. The Vega-Mosquera-Hurtado technique §4.2 gives the assignment criteria (see §4 below).

---

## 4. FP/OO assignment criteria (from the technique)

When proposing a refactoring, the command justifies each move with one of these criteria (Ch. 4 §4.2 of the manuscript):

1. **Nature of state** — encapsulated mutable state with stable identity → OO; transformation of immutable values → FP.
2. **Form of polymorphism** — dynamic dispatch by receiver type → OO (subtypes); dispatch by closed data structure → FP (ADT + pattern matching).
3. **Composition vs inheritance** — function composition for data pipelines; inheritance/object composition for runtime structural variability.
4. **Anticipated change frontier (Expression Problem)** — new operations over fixed data → FP (open functions); new data for fixed operations → OO (classes).
5. **Concurrency and reactivity** — actors and reactivity cross both paradigms; prefer abstractions that separate the "what" (FP) from the "who" (OO actor).
6. **Simultaneous extensibility frontier (data × operations)** — if the change opens *both* dimensions, Object Algebras / tagless final offers the canonical closure.

**Anchors**: Coplien (2000) [#31], Vranic (2009) [#32], Narbel (2007) [#25], Castro (2020) [#30], Oliveira & Cook (2012).

---

## 5. Abstraction mechanisms (M1-M6)

When the command proposes a refactoring, it tags it with one of these mechanisms (Ch. 4 §4.2 of the manuscript, derived from the cross of QA corpus × Narbel):

| # | Mechanism | Reformulates | Main anchor |
|---|---|---|---|
| **M1** | Object algebras / Tagless Final | Factory + Visitor | Oliveira & Cook 2012; Oliveira & van der Storm 2015 |
| **M2** | Type classes / Generalized Interfaces | Adapter, Strategy | JavaGI (Wehr & Thiemann); Simplicitly (Odersky) |
| **M3** | Algebraic types + Pattern Matching | Visitor | Matching Objects with Patterns (Emir); Open data types (Loh & Hinze) |
| **M4** | HOF + Closures + FP composition | Strategy, Command, Iterator | Bringert & Ranta; Sousa & Ferreira; Crichton |
| **M5** | Reactive signals / Actors / Continuations | Observer | Salvaneschi (RP comprehension); Haller & Odersky (Actors) |
| **M6** | Composition of paradigms by subdomain | Architectural level | Coplien; Vranic |

---

## 6. Pattern catalog (16)

Each refactoring proposal can invoke one of the 16 patterns from Ch. 4 §4.5.

| # | Pattern | Origin | Hybrid reformulation |
|---|---|---|---|
| 1 | Strategy | GoF (OO) | Lambda / HOF as injectable strategy |
| 2 | Visitor | GoF (OO) | Pattern matching over ADT; strong alternative: Object Algebra |
| 3 | Iterator | GoF (OO) | Stream + map/filter/reduce; datatype-generic traversal |
| 4 | Decorator | GoF (OO) | Function composition / wrapping with HOF |
| 5 | Observer | GoF (OO) | Reactive streams / declarative signals |
| 6 | Extensible State Machine | OO | Generic types + functional dispatch |
| 7 | Datatype-Generic | FP | OO datatype-generic with typeclasses |
| 8 | Almost Compositional | FP | FP pattern applied in OO with monads |
| 9 | Component Prototyping | Hybrid | Multi-parameter type classes + functional dependencies |
| 10 | Actor Unification | Concurrent | Actors that unify threads and events via continuations |
| 11 | **Object Algebra** | FP+OO | Generalizes Factory + Visitor; solves the Expression Problem |
| 12 | **Immutable Factory Method** | OO with FP values | Parametric generics + default methods + type-specific factory |
| 13 | **Type Class / Generalized Interface** | FP+OO | Polymorphism without inheritance, open to new types |
| 14 | **Extractor** | FP+OO | Adapter between OO encapsulation and FP pattern matching |
| 15 | **Parser Combinators** | FP in OO host | FP composition of parsers with OO types |
| 16 | **Tagless Final / Free Monad** | FP+OO | Polymorphic abstract algebra, multi-target |

---

## 7. Cross-paradigm refactoring categories

Every refactoring proposal from the command is tagged with one of these 8 categories (Ch. 4, transversal):

1. **Pattern replacement (GoF → FP construct)** — Strategy → HOF; Command → lambda; Visitor → algebra.
2. **Loop/iteration → pipeline/HOF** — imperative loops to `map/filter/reduce`.
3. **Mutable → immutable** — copies instead of mutation (Hashimoto et al., 2023).
4. **Class hierarchy → ADT/pattern matching** — closed hierarchies to sealed traits.
5. **Object Algebras / tagless final** — Expression Problem.
6. **Intra-FP refactoring** — Thompson 2005 (HaRe).
7. **Not applicable** — code is already optimal or change is irrelevant.
8. **Other** — emergent; review for promotion to a category.

---

## 8. Lightweight UML profile (stereotypes)

When the command sketches a design proposal (in pseudocode or text-UML), it uses these 6 stereotypes from Ch. 4 §4.3:

| Stereotype | Meaning |
|---|---|
| `<<algebra>>` | Family of polymorphic operations over an ADT, closed by interpretation |
| `<<signal>>` | Value that changes over time, with declarative dependencies |
| `<<var>>` | Encapsulated mutable state with stable identity |
| `<<typeclass>>` | Polymorphic behavior without inheritance, open to new types |
| `<<extractor>>` | Decomposition pattern that adapts OO values to a pattern-matchable form |
| `<<actor>>` | Concurrent unit with mailbox, identity, and behavior via messages |

Sequence-diagram markers: `<<react>>`, `<<send>>`, `<<receive>>`, `<<continuation>>`.

---

## 9. Business rules (qua domain)

The command, alongside paradigm, identifies **implicit business rules** and separates them from mechanics.

**Heuristics for detection**:
- Magic constants with semantic meaning (`MAX_RETRIES = 3`, `TAX_RATE = 0.19`).
- Validations embedded in the flow (`if (age < 18) reject(...)`).
- Derived calculations that mix formula with orchestration.
- Implicit state machines (`status === 'pending'`).
- Inline permissions / roles.

**Treatment in the report**: list each rule with (i) location, (ii) statement in natural language, (iii) suggested paradigm to encapsulate it (FP if it is pure transformation; OO if it carries state/identity), (iv) applicable pattern from the catalog.

---

## 10. Canonical bibliographic anchors

The command's metrics and concepts come exclusively from:

- **Eden & Mens (2006)** — Measuring software flexibility. Reference [1] of the original proposal.
- **Kallel et al. (2018)** — Architecture Constraints [#27 in the State of the Art].
- **Heinzl & Schreibmann (2018)** — UML Extensions for FP [#26].
- **Motara (2021)** — Typed FP Modelling [#28].
- **Heithoff (2023)** — Multi-Viewpoint Modeling [#33].
- **Coplien (2000)** — Multi-Paradigm Design [#31].
- **Vranic (2009)** — Feature Modeling [#32].
- **Narbel (2007)** — Multiparadigmatic Design Patterns [#25].
- **Castro (2020)** — Paradigm Impact [#30].
- **Oliveira & Cook (2012)** — Extensibility for the Masses (corpus DE, score 4.5).

Any additional reference the user wants to add must be in the QA > 2.0 corpus of the thesis (68 effective studies).

---

## 11. What the command **does not** do

- It does not invent metrics outside §1-§2.
- It does not classify paradigms other than OO/FP/procedural (no logic, no DSL, unless the case study introduces them).
- It does not deliver a verdict "OO is better" or "FP is better": Castro (2020) [#30] shows that paradigm alone does not determine quality.
- It does not execute the code: analysis is static over the provided text.
- It does not promise a quantitative improvement in Flex-Score if the code is already at its domain optimum (refactoring category "Not applicable").
