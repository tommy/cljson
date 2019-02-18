#!/bin/bash -exu

deploy_dir="target/deploy/"
dry_run=false

# Parse options
set +x
for arg in "$@"; do
  shift
  case "$arg" in
    --dry-run|-n) dry_run=true ;;
    *) echo "Option doesn't exist"; exit 1 ;;
  esac
done
set -x

# Clean
rm -rf target

# Build
clj -A:fig:package

# Copy
mkdir -p "$deploy_dir"
cp target/production/public/main.js "$deploy_dir"
cp resources/public/index.html "$deploy_dir"

# Deploy
if [ "$dry_run" != "true" ] ; then
  aws --profile cljson s3 sync target/deploy/ s3://cljson.com --delete
else
  echo "Dry run, not deploying."
fi
