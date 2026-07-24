# 02 — Lightweight UML profile and paradigm detectors (§4.3)

> Profile that extends UML class and sequence diagrams through **stereotypes**
> `<<...>>` and **tagged values** `{...}`. It does not modify the UML metamodel, so it
> remains compatible with standard tools. It continues the functional extensions of
> Heinzl & Schreibmann (2018) and answers the notation gap pointed out by Motara (2021).
>
> The agent uses these stereotypes **only in the "after"**: in **Phase 5 (intervention)** it
> annotates them as comments on the classes/functions of the refactored code, to make the
> design decisions visible. The **baseline** of Phase 1 ("before" state) is drawn in
> **neutral standard UML, without this profile**: this way the initial snapshot is not biased
> by the technique's lens and the before/after contrast makes the variation **visible** — the
> very appearance of these stereotypes is part of the delta.

---

## Base extensions for the class diagram (Table 4.2)

| Functional concept | Notation | Description |
|---|---|---|
| Immutability | `{immutable}` | Tag on a class or attribute: the state does not change after instantiation |
| Pure function | `<<pure>>` | Stereotype on a method: referential transparency; no read or write of global state |
| Value object | `<<ValueObject>>` | Stereotype on a class: equality by value, not by reference; must be immutable |
| Higher-order function | signature `(T) -> R` | The arrow signature indicates that the parameter is **behavior**, not data |

**Examples:** `Coordenada {immutable}` with attributes `x, y` that never change; its method
`distancia(otra): Real <<pure>>` guarantees referential transparency; `Dinero <<ValueObject>>`
is compared by value; `ordenar(comparador: (T, T) -> Entero)` declares through the arrow
signature that the injected parameter is behavior.

## Additional class stereotypes (derived from the corpus)

| Stereotype | On | Meaning | Anchor |
|---|---|---|---|
| `<<algebra>>` | interface | Generic factory that combines creation with interpretation; solves the Expression Problem without `accept` methods | Oliveira & Cook 2012; Oliveira 2015 |
| `<<var>>` | attribute | Observable mutable variable that serves as a source for *signals* | Salvaneschi 2017 |
| `<<signal>>` | attribute | Variable whose value updates automatically when its declarative dependencies change | Salvaneschi 2017 |
| `<<typeclass>>` | interface | Interface implementable **retroactively** (like Haskell *type classes*); dynamic dispatch without coupling the OO hierarchy | JavaGI (Wehr et al.) |
| `<<extractor>>` | method | `unapply` method that decomposes values for pattern matching, separating structure from representation | Emir et al. |
| `<<actor>>` | class | Component with a message queue, asynchronous messaging, and encapsulated state; unifies thread-based and event-based concurrency | Haller & Odersky |

## Markers for the sequence diagram

**Base:**
- `<<lambda>>` — on the message arrow: the argument passed is an **executable function**.
  E.g.: `coleccion.ordenar(<<lambda>> comparador)`.
- `<<Lazy>>` — note adjacent to the message: the function passed is executed **later**, not at
  call time. E.g.: `registro.definir(<<Lazy>> crearRecurso)` (evaluated on first access).

**Additional (from the corpus):**
- `<<react>>` — on arrows that propagate events in *signal* graphs: the arrow is not an
  explicit call but the **automatic propagation** of the change through the graph
  (Salvaneschi 2017).
- `<<send>>` / `<<receive>>` — **asynchronous messages between actors** (Haller & Odersky). E.g.:
  `productor <<send>> procesar(tarea)` with its `<<receive>>` in the consumer actor's loop.
- `<<continuation>>` — on arrows that **capture control** for later resumption; the
  message suspends the flow while waiting for the next event and resumes it upon receipt
  (Chin & Millstein 2008).

---

## Paradigm detectors (for Phase 1)

To report the **paradigm balance**, the agent counts OO and FP proxies in the code.

### OO signals
- Classes with mutable state and methods that modify it.
- Inheritance, subtype polymorphism (dynamic dispatch by receiver).
- Interfaces / abstract classes.
- Classic GoF patterns (Visitor, Strategy, Observer, Decorator, Command, Iterator).
- Encapsulation: getters/setters, access modifiers.
- Stable object identity (environments, sessions, connections).

### FP signals
- Pure functions without side effects.
- Higher-order functions (HOF): `map`, `filter`, `reduce`, `flatMap`, callbacks, lambdas.
- Immutability: `const`, `final`, `val`, copies instead of mutation.
- Pattern matching / `match` / *destructuring*.
- Algebraic data types / *sealed traits* / discriminated unions.
- Function composition, currying, partial application.
- Explicit monads (`Option`, `Result`, `Either`, `Maybe`, `IO`).

### Paradigm balance

```
Balance = |OO_signals - FP_signals| / (OO_signals + FP_signals)
```

- `Balance ≈ 0`: balanced code (effective multiparadigm).
- `Balance > 0.7`: mono-paradigm code; refactoring candidate **if** the signals of the
  *other* paradigm would serve the domain.

> The balance is neither good nor bad in itself. Coplien (2000): the domain chooses the
> paradigm. The assignment criteria are in `01-criterios-decision.md`.
