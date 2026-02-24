#!/usr/bin/env bash
set -euo pipefail

BRANCH="${1:-dev}"
ENV_NAME="${2:-DEV}"

cd /var/www/html/mycellius

git fetch origin
git checkout -B "$BRANCH" "origin/$BRANCH"
git reset --hard "origin/$BRANCH"
git clean -fd

export MYCELLIUS_ENV="$ENV_NAME"
export MYCELLIUS_VERSION="$(git rev-parse --short HEAD)"

docker compose -f infra/stack/docker-compose.yml up -d --build
echo "Deployed $ENV_NAME / $MYCELLIUS_VERSION"