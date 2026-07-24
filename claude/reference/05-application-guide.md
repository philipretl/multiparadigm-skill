# 05 — Methodological application guide (§4.6)

> The technique intervenes in **detailed design** in two modes that share criteria,
> notation, and catalog; only the entry point changes:
>
> - **New components:** the criteria and the notation guide responsibility assignment
>   from the requirements (steps 1, 3, and 4).
> - **Already-built systems (redesign):** the present patterns are detected, reformulated
>   with the catalog, and the change is applied by **refactoring** (steps 2, 5, and 6). The
>   refactoring here is a design review over existing code preserving the observable
>   behavior.

The `/multiparadigm` agent operates mainly in **redesign mode**. The six-step guide maps
directly onto the agent's six phases (`agent.md` §3).

---

## The six steps

1. **Decomposition and classification.** Decompose the requirements/code into candidate
   components and apply **Table 4.1** to each one.
   *Output:* list of components classified as mutable entity (OO) or value/function (FP).
   -> *Agent Phase 1.*

2. **Detection of existing patterns.** Inventory the patterns present (Strategy, Visitor,
   Observer…) using the **catalog** (`03-pattern-catalog.md`) as a checklist. Every
   detected classic pattern is a direct reformulation candidate, with its associated M1–M5
   mechanism.
   -> *Agent Phase 2.*

3. **Definition of hybrid interfaces.** Design the interfaces of the main classes (OO) and, at
   the **variation points** that require flexibility, replace single-method interfaces with
   **function-typed parameters**. Apply Open/Closed by functional composition
   (Narbel 2007).
   -> *Agent Phases 3–4.*

4. **Visual modeling.** Build the class and sequence diagrams with the notation of
   `02-uml-profile.md`, explicitly marking the `<<pure>>` methods and the `<<ValueObject>>`
   classes so the decisions stay visible.
   -> *Agent Phase 1*: produces the **baseline diagram(s)** in Mermaid ("before" state),
   in **neutral standard UML without the technique's profile**, as a result item of the analysis
   (one per subdomain). -> *Agent Phase 5*: applies the profile (stereotypes) to the "after"
   code. The contrast between the neutral snapshot and the annotated design exhibits the
   variation that Phase 6 quantifies.

5. **Refactoring towards immutability.** Review the preliminary design to reduce shared
   mutable state. If an OO class has a majority of `<<pure>>` methods, evaluate converting it
   into a functional module or a `<<ValueObject>>`; if a procedural sequence can be replaced by
   function composition, do it.
   -> *Agent Phase 5.*

6. **Validation against the evaluation model.** Before descending to code, contrast the
   resulting design with the model in `04-evaluation-model.md`: declare the **anticipated
   evolution steps** and verify that the decisions **absorb** them (the anticipated changes are
   resolved with bounded cost under the model's metrics).
   -> *Agent Phase 6.*

> The steps can be **iterated** throughout the design. The *criterion -> decision*
> traceability is maintained in all of them.
