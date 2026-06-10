# GitHub Copilot Instructions — multiparadigm-skill

When the user invokes `/multiparadigm` in Copilot Chat (or asks for a multiparadigm analysis):

1. Read the canonical model from `shared/MODEL.md` of this repo and the canonical prompt from `shared/PROMPT.md`. They are the source of truth for vocabulary, metrics, and output format.
2. Apply the prompt to the code the user provides (selection, file, or workspace).
3. Return the report in the exact Markdown structure defined in `shared/PROMPT.md` §OUTPUT.

Hard rules:
- Do not invent metrics outside `shared/MODEL.md` §1-§2.
- Do not deliver a verdict "OO is better" or "FP is better"; follow Coplien (2000): paradigm is chosen by domain.
- Report honest deltas; if Flex-Score drops, say so.
- Anchor each metric to its bibliographic reference (Eden & Mens 2006; Kallel et al. 2018; Heinzl & Schreibmann 2018; Heithoff 2023).
- Respond in the user's language (default: neutral technical English).
- Default behavior is same-language refactoring; only switch languages if the user passes `--target <lang>` or the source language lacks the required mechanism.

Anchoring (Vega-Mosquera-Hurtado, Universidad del Cauca, 2026):
- 3 flexibility dimensions (extensibility, modularity, variation points).
- 6 FP/OO assignment criteria.
- 6 abstraction mechanisms M1-M6.
- 16-pattern catalog.
- 8 refactoring categories.
- 6 UML stereotypes (`<<algebra>>`, `<<signal>>`, `<<var>>`, `<<typeclass>>`, `<<extractor>>`, `<<actor>>`).
