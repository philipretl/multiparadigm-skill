#!/usr/bin/env bash
# Renders to SVG every Mermaid diagram (.mmd) in a multiparadigm output
# directory, using @mermaid-js/mermaid-cli.
#
# Usage: scripts/render-mmd.sh <multiparadigm-dir>
#   e.g.  scripts/render-mmd.sh multiparadigm-2026-07-21
#
# If mermaid-cli is not available (no network / no Chromium), the render of that
# file fails but the script continues and reports the pending ones at the end.
set -uo pipefail

dir="${1:?Usage: render-mmd.sh <multiparadigm-dir>}"
[ -d "$dir" ] || { echo "Directory does not exist: $dir" >&2; exit 1; }

ok=0; fail=0; found=0
while IFS= read -r -d '' mmd; do
  found=1
  svg="${mmd%.mmd}.svg"
  if npx -y @mermaid-js/mermaid-cli -i "$mmd" -o "$svg" >/dev/null 2>&1; then
    echo "✓ $svg"
    ok=$((ok + 1))
  else
    echo "✗ $mmd  (render failed; keep the .mmd and retry with mermaid-cli)" >&2
    fail=$((fail + 1))
  fi
done < <(find "$dir" -type f -name '*.mmd' -print0)

[ "$found" -eq 1 ] || { echo "No .mmd found in $dir"; exit 0; }
echo "Render: $ok ok, $fail pending."
[ "$fail" -eq 0 ]
