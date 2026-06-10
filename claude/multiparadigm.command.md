---
name: /multiparadigm
description: Multiparadigm OO+FP analysis with flexibility delta
allowed-tools: Read, Grep, Glob, Edit, Write
---

# /multiparadigm

Invokes the `multiparadigm` skill. If you receive arguments:

- `<path>`: analyze the file or directory.
- `--domain "<description>"`: domain context.
- `--target <lang>`: propose the refactoring in another multiparadigm language (Scala, Kotlin, F#, TypeScript). Default behavior is same-language refactoring.

If there are no arguments, ask the user to paste the code or provide the path.

Follow the 6 phases of `shared/PROMPT.md` and return the report in the mandatory Markdown format.
