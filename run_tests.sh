#!/bin/bash
set -Eeuo pipefail

HERE="$(readlink -f "$(dirname "$0")")"
SCRIPT="$(mktemp).scd"

cat >"$SCRIPT" <<EOF
if (Quarks.isInstalled("Timing").not) {
  Quarks.install("$HERE");
};
0.exit;
EOF
cat "$SCRIPT"
sclang "$SCRIPT"

RUN_CMDS="$(find "$HERE/tests" -name "*.sc" | xargs -n 1 basename | sed 's/\.sc/.run;/')"

cat >"$SCRIPT" <<EOF
Task({
  try {
    UnitTest.reportPasses = false;
    $RUN_CMDS
    10.wait;
    "Hacky timout finished. I hope our tests finished running :/".postln;
    UnitTest.failures.size.exit;
  } {|error|
    error.reportError;
    1.exit;
  };
}).start();
EOF
cat "$SCRIPT"
sclang "$SCRIPT"
