# Copilot adapter — multiparadigm-skill

## Installation

Copy to the target repo:

1. `.github/prompts/multiparadigm.prompt.md` → into the repo where `/multiparadigm` will be invoked from Copilot Chat.
2. `.github/copilot-instructions.md` (or the contents of `copilot-instructions.md` from this adapter) → workspace-global instructions for Copilot.
3. Make sure `shared/MODEL.md` and `shared/PROMPT.md` are in the target repo or referenced as a submodule / package.

## Usage

In Copilot Chat:

```
/multiparadigm
```

or, with code selected:

```
/multiparadigm explain which patterns you detect and propose a refactoring
```

Copilot responds with the report in the Markdown format from `shared/PROMPT.md` §OUTPUT.

## Additional configuration

If the workspace has a `.vscode/settings.json`, ensure `chat.promptFiles` points to `.github/prompts`.
