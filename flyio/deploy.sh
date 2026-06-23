#!/bin/bash
# ==============================================================================
# EduFlow – Fly.io Deployment Script
# ==============================================================================
# Usage: ./flyio/deploy.sh [be|fe|all]
# ==============================================================================

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log() { echo -e "${GREEN}[DEPLOY]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# Check flyctl is installed
command -v fly >/dev/null 2>&1 || error "flyctl is not installed. Run: curl -L https://fly.io/install.sh | sh"

# Check logged in
fly auth whoami >/dev/null 2>&1 || error "Not logged in. Run: fly auth login"

deploy_backend() {
    log "Deploying Backend..."
    cd "$(dirname "$0")/../BE_EduFlow"

    # Check if app exists, create if not
    if ! fly status -a eduflow-be-dev >/dev/null 2>&1; then
        log "Creating backend app..."
        fly launch --name eduflow-be-dev --region sin --no-deploy --yes
    fi

    fly deploy -a eduflow-be-dev
    log "Backend deployed! URL: https://eduflow-be-dev.fly.dev"

    # Health check
    log "Waiting for health check..."
    sleep 10
    if curl -sf https://eduflow-be-dev.fly.dev/actuator/health > /dev/null; then
        log "Backend health check: PASSED ✓"
    else
        warn "Backend health check failed. Check logs: fly logs -a eduflow-be-dev"
    fi
}

deploy_frontend() {
    log "Deploying Frontend..."
    cd "$(dirname "$0")/../FE_EduFlow"

    # Check if app exists, create if not
    if ! fly status -a eduflow-fe-dev >/dev/null 2>&1; then
        log "Creating frontend app..."
        fly launch --name eduflow-fe-dev --region sin --no-deploy --yes
    fi

    fly deploy -a eduflow-fe-dev
    log "Frontend deployed! URL: https://eduflow-fe-dev.fly.dev"

    # Health check
    log "Waiting for health check..."
    sleep 10
    if curl -sf https://eduflow-fe-dev.fly.dev/ > /dev/null; then
        log "Frontend health check: PASSED ✓"
    else
        warn "Frontend health check failed. Check logs: fly logs -a eduflow-fe-dev"
    fi
}

TARGET=${1:-all}

case $TARGET in
    be|backend)
        deploy_backend
        ;;
    fe|frontend)
        deploy_frontend
        ;;
    all)
        deploy_backend
        cd "$(dirname "$0")"
        deploy_frontend
        ;;
    *)
        echo "Usage: $0 [be|fe|all]"
        exit 1
        ;;
esac

echo ""
log "============================================"
log "  Deployment Complete!"
log "============================================"
log "  Frontend: https://eduflow-fe-dev.fly.dev"
log "  Backend:  https://eduflow-be-dev.fly.dev"
log "  Health:   https://eduflow-be-dev.fly.dev/actuator/health"
log "============================================"
