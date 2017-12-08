#!/bin/sh

CR='/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */
'

grep -rL "Koninklijk" --include \*.h . | while read -r i; do echo "$CR" | cat - "$i" > "$i.tmp"; mv "$i.tmp" "$i"; done
grep -rL "Koninklijk" --include \*.c . | while read -r i; do echo "$CR" | cat - "$i" > "$i.tmp"; mv "$i.tmp" "$i"; done
grep -rL "Koninklijk" --include \*.java . | while read -r i; do echo "$CR" | cat - "$i" > "$i.tmp"; mv "$i.tmp" "$i"; done
