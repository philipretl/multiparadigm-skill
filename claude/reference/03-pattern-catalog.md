# 03 — Catalog of reformulated patterns (§4.4)

> Sixteen patterns that update classic references (GoF) and recent functional patterns for
> **hybrid** use. Each pattern connects to a criterion from `01-decision-criteria.md`
> and to a mechanism **M1–M5**. In Phase 2 the agent uses them as a **checklist**
> to detect existing patterns; in Phase 5, as a reformulation repertoire.

For each pattern, the catalog describes: intent, context, forces, structure (UML profile
notation), usage example, applicability criteria, and consequences (quality attributes
covered).

---

## Catalog (i) — GoF/OO patterns reformulated with functional mechanisms

| # | Pattern | Hybrid reformulation | Mech. |
|---|---|---|---|
| 1 | **Strategy** | Lambda as injectable strategy; replaces the Strategy/ConcreteStrategy hierarchy with a functional parameter | M4 |
| 2 | **Visitor** | Pattern matching + external function; alternative: object algebra (solves data × operations at once) | M3 / M1 |
| 3 | **Iterator** | Stream + `map`/`filter`/`reduce`; *datatype-generic* traversal | M4 |
| 4 | **Decorator** | Function composition / *wrapping* with HOF (when the decorators are pure and stateless between calls) | M4 |
| 5 | **Observer** | *Reactive streams* / declarative *signals*; better comprehensibility and lower coupling (empirical evidence) | M5 |
| 6 | **Extensible State Machine** | Extensible state machine with generic types and *delimited continuations*; modular extension of states and events | M5 |

## Catalog (ii) — Post-GoF patterns and mechanisms (from the corpus)

| # | Pattern | Origin | Reformulation | Mech. |
|---|---|---|---|---|
| 7 | **Datatype-Generic** | FP | Datatype-generic OO with typeclasses / generic interfaces; solves the Expression Problem two-dimensionally | M2 |
| 8 | **Almost Compositional** | FP | Functional pattern in OO with monads / applicatives; saves repetitive code in typed transformations | M4 |
| 9 | **Component Prototyping** | Hybrid | Type-level approach with type classes and functional dependencies; rapid prototyping with type safety | M2 |
| 10 | **Actor Unification** | Concurrent | Actors that unify threads and events via continuations | M5 |
| 11 | **Object Algebra** | Post-GoF | Generalization of Factory + Visitor without `accept` methods; solves the Expression Problem, two-dimensional extension | M1 |
| 12 | **Immutable Factory Method** | Post-GoF | Generics + default methods + type-specific factory to reuse methods over immutable types | M2 |
| 13 | **Type Class / Generalized Interface** | Post-GoF | Polymorphism without inheritance, open to new types without touching definitions; makes Adapter, Factory, and parts of Visitor obsolete | M2 |
| 14 | **Extractor** (Active Pattern) | Post-GoF | Adapter between OO encapsulation and FP pattern matching (`unapply`, active patterns) | M3 |
| 15 | **Parser Combinators** | Post-GoF | FP composition of parsers with OO types in a multiparadigm language | M4 |
| 16 | **Tagless Final / Free Monad** | Post-GoF | Polymorphic abstract algebra for multi-target interpretation of the same AST; separates syntax from semantics | M1 |

---

## Applicability notes per pattern

- **Strategy** -> when the variability is pure behavior and the OO hierarchy adds
  indirection without value. Consequence: extensibility and configurability without new classes.
- **Visitor** -> replaces double dispatch with extractors/*case classes* (M3) or with object
  algebras (M1) when extension of data **and** operations is needed.
- **Iterator** -> higher-order pipelines instead of external iterators; compositionality,
  no mutable iteration state, implicit parallelization.
- **Observer** -> reactive *signals*; better maintainability and modularity.
- **Object Algebra** / **Tagless Final** -> the canonical mechanism when the anticipated
  change opens **both** dimensions (Expression Problem).
- **Type Class / Generalized Interface** -> give behavior to library types without
  modifying or wrapping them.
- **Extractor** -> APIs that expose pattern matching over types whose internal representation
  must stay hidden.
- **Actor Unification** -> unifies thread-based and event-based concurrency; modularity and
  tunable performance.

---

## Business rules (for Phase 3)

The agent identifies **implicit business rules** and separates them from the mechanics.

**Detection heuristics:**
- Magic constants with semantic meaning (`MAX_RETRIES = 3`, `VAT_RATE = 0.19`).
- Validations embedded in the flow (`if (age < 18) reject(...)`).
- Derived computations that mix formula with orchestration.
- Implicit state machines (`status === 'pending'`).
- Inline permissions / roles.

**Treatment in the report:** for each rule, (i) location, (ii) statement in natural
language, (iii) degree of mixing with the mechanics, (iv) suggested paradigm to encapsulate it
(**FP** if it is a pure transformation; **OO** if it carries state/identity; **hybrid** if
applicable), and (v) applicable pattern from the catalog.
