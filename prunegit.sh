#!/bin/bash
git filter-branch --index-filter "git rm -rf --cached --ignore-unmatch $1" --prune-empty --tag-name-filter cat -- --all
git for-each-ref --format="%(refname)" refs/original/ | xargs -n 1 git update-ref -d
