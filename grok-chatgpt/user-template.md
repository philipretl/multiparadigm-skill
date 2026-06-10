# Invocation template — user (Grok / ChatGPT / generic)

> After pasting `system-prompt.md` into the assistant's configuration, use this template as the user message to invoke `/multiparadigm`.

---

## Mode 1 — Paste code directly

```
/multiparadigm

Language: <java|python|js|ts|scala|kotlin|other>
Subdomain (optional): <CRUD | AST/interpreter | ETL pipeline | HTTP controller | parser | other>
Anticipated changes (optional, default: 3 standard): <list if you want to customize>

Code:
```<lang>
<paste the code to analyze here>
```
```

## Mode 2 — File reference (assistant with file access)

```
/multiparadigm @<path-to-file>

Language: <optional, auto-detected>
Subdomain (optional): <...>
Target (optional): <scala | kotlin | typescript | f#> — propose the refactoring in another multiparadigm language
```

## Mode 3 — Dialog only

```
/multiparadigm
I have been thinking about refactoring this module of my project. I'll paste the code and want the full report, especially the flexibility delta before and after.

```<lang>
<code>
```
```

---

## Reminder for the user

The assistant returns a Markdown document with 7 sections:
1. Detection (OO/FP balance)
2. Detected patterns
3. Business rules
4. Technique application (criterion + mechanism + category)
5. Refactoring proposal (with after code)
6. Flexibility delta (extensibility, modularity, variation points + Flex-Score)
7. Bibliographic anchors

If the report comes out incomplete, ask explicitly "complete section N" — do not accept summaries that omit sections.
