# 01 — OO/functional decision criteria (§4.2)

> Source of truth for **paradigm assignment**. Every Phase 4 decision of the agent must cite
> one of these criteria. They do not start from scratch: they synthesize the
> domain->paradigm assignment of Coplien (2000) and Vranic (2009), Narbel's (2007)
> paradigmatic classification of patterns, and the system of abstraction mechanisms M1–M6.

The guiding principle **M6** (paradigm assignment per subdomain) governs the **first**
decision; mechanisms **M1–M5** materialize the detailed-design decisions within each
subdomain. The criteria are organized in four dimensions: **state management,
behavior, structure, and concurrency**.

---

## Table 4.1 — OO/Functional decision table for detailed design

| Problem characteristic | Recommended approach | Flexibility rationale |
|---|---|---|
| Entity with identity and mutable state | **OO** (classes with encapsulation) | Encapsulation protects state integrity |
| Data transformation flow (ETL) | **Functional** (*pipelines*) | Modularity through function composition |
| Simple parameterizable behavior | **Functional** (lambdas) | Lightweight configurability without class overhead |
| Complex parameterizable behavior | **OO** (interfaces / Strategy) | Polymorphism with state and interchangeable implementations |
| Frequent new data types | **OO** (open subtype hierarchies) | Extensibility via subtype polymorphism |
| Frequent new operations | **Functional** (sealed ADT + *pattern matching*, HOF) | Extensibility via new functions over closed types |
| New types **and** operations simultaneously | **Hybrid** (object algebras / *tagless final*) | Two-dimensional extensibility: solves the *Expression Problem* |
| High concurrency | **Functional** (immutability) | Reduced side effects and locking |

---

## Dimension 1 — State and data management

**Criterion 1.1 — Identity vs. Value.**
- **OO** when the component is a **domain entity** that requires unique identity and a
  life cycle (it is born, changes, dies) while keeping its identity: user, session, order. The
  encapsulation protects the internal mutable state.
- **Functional** when the component is a **value or informational datum without its own
  identity** that must be immutable: coordinates, amounts, DTOs, domain events. Immutability
  avoids side effects.

**Criterion 1.2 — Data transformation (M4).**
- **OO** when the manipulation demands keeping a history of complex internal states or
  state machines.
- **Functional** when a **pipeline**-style flow is required, where the output of one operation
  is the input of the next without modifying the original datum (referential transparency).

## Dimension 2 — Behavior and extensibility

**Criterion 2.1 — Strategies and algorithms (variation points; M4).**
- **OO** when the strategy to inject is **complex**, keeps its own state, or has
  multiple related methods (e.g. a database driver).
- **Functional (lambdas)** when the behavior is a **single, focused operation**: a
  filtering criterion, a tax-calculation formula. HOFs inject behavior without the verbosity of
  anonymous classes.

**Criterion 2.2 — Functionality extension (extensibility; M1, M3).**
- **OO** (inheritance, polymorphism) when growth is expected by adding **new data types** or
  variants, while operations remain stable (Open/Closed via inheritance).
- **Functional** (composition) when growth is expected by adding **new operations** over
  existing types. HOFs compose new abstractions without touching the existing ones.

> This dilemma is the practical formulation of the **Expression Problem**. When the
> anticipated change opens **both** dimensions (new types *and* new operations), the indicated
> mechanism is **object algebras** or **tagless final** encodings (M1).

## Dimension 3 — Structure and modularity

**Criterion 3.1 — Architectural boundaries (M6).**
- **OO** for the static structure: service interfaces, controllers, dependency
  injection. OO is superior for defining contracts and independent modules.
- **Functional** for the **internal implementation** of those modules (the body of the
  methods), especially the pure business logic inside the boundaries defined by the classes.

## Dimension 4 — Concurrency and side effects

**Criterion 4.1 — Parallel processing (M5).**
- **OO** when explicit synchronization and shared-resource management are required
  (locks, semaphores); applicable, but error-prone.
- **Functional** in high concurrency where **immutability** and pure functions are exploited:
  without shared mutable state, race conditions disappear and safe parallelism becomes easier.

---

## Abstraction mechanisms (M1–M6) — key question

| Mech. | Key question | Mechanism |
|---|---|---|
| **M1** | How do I grow types and operations simultaneously without touching what exists? | Data are functions awaiting an interpreter (polymorphic algebraic signature) |
| **M2** | How do I give new behavior to existing types without modifying or wrapping them? | Someone external provides the implementation (*type class instance*): retroactive ad hoc polymorphism |
| **M3** | How do I decompose by cases without the Visitor's *accept/visit* scaffolding? | Sealed ADT + exhaustive *match*; the compiler verifies that no case is missing |
| **M4** | How do I represent pure behavior without raising class hierarchies? | A function as a value: lambda as strategy, closure as command, pipeline as iterator |
| **M5** | How do I propagate changes without coupling subjects to observers? | Declarative dependency graph: the *runtime* propagates the change, not the programmer |
| **M6** | Which paradigm governs this subdomain? | Composition of paradigms per subdomain (architectural level; guiding principle) |
