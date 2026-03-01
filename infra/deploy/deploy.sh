#!/usr/bin/env bash
set -euo pipefail

BRANCH="${1:-dev}"      # dev | test | prod
ENV_NAME="${2:-DEV}"    # DEV | TEST | PROD

# Dossier de déploiement (repo cloné "stable") :
# - tu peux le forcer via DEPLOY_DIR (recommandé dans CI)
# - sinon auto-détection (DEV/TEST vs PROD)
DEPLOY_DIR="${DEPLOY_DIR:-}"

if [[ -z "$DEPLOY_DIR" ]]; then
  if [[ -d "/var/www/html/mycellius/.git" ]]; then
    DEPLOY_DIR="/var/www/html/mycellius"
  elif [[ -d "/opt/mycellius/.git" ]]; then
    DEPLOY_DIR="/opt/mycellius"
  else
    echo "ERROR: Impossible de trouver le repo de déploiement."
    echo "Attendu: /var/www/html/mycellius/.git ou /opt/mycellius/.git"
    echo "Fix: exporte DEPLOY_DIR=/chemin/du/repo avant d'exécuter le script."
    exit 1
  fi
fi

echo "[deploy] DEPLOY_DIR=$DEPLOY_DIR BRANCH=$BRANCH ENV=$ENV_NAME"

cd "$DEPLOY_DIR"

# Sécurité : on doit être dans un repo Git
git rev-parse --is-inside-work-tree >/dev/null

# Sync propre sur la branche distante
git fetch origin
git checkout -B "$BRANCH" "origin/$BRANCH"
git reset --hard "origin/$BRANCH"
git clean -fd

export MYCELLIUS_ENV="$ENV_NAME"
export MYCELLIUS_VERSION="$(git rev-parse --short HEAD)"

COMPOSE_FILE="infra/stack/docker-compose.yml"
if [[ ! -f "$COMPOSE_FILE" ]]; then
  echo "ERROR: Compose introuvable: $DEPLOY_DIR/$COMPOSE_FILE"
  exit 1
fi

# Force un nom de projet stable (évite les surprises si le dossier change)
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-stack}"

docker compose -p "$COMPOSE_PROJECT_NAME" -f "$COMPOSE_FILE" up -d --build

echo "[deploy] Deployed $ENV_NAME / $MYCELLIUS_VERSION"