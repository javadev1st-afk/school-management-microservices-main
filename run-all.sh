#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="${ROOT_DIR}/.run-logs"
EUREKA_URL="${EUREKA_URL:-http://localhost:8761}"

SERVICE_MODULES=(
  "user-service"
  "academic-service"
  "payment-service"
  "notification-service"
  "communication-service"
  "utility-service"
  "api-gateway"
)

PIDS=()

log() {
  printf '[run-all] %s\n' "$*"
}

fail() {
  printf '[run-all] ERROR: %s\n' "$*" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || fail "Required command not found: $1"
}

stop_services() {
  local exit_code=$?
  local pid
  trap - EXIT INT TERM
  for pid in "${PIDS[@]}"; do
    if kill -0 "$pid" 2>/dev/null; then
      kill "$pid" 2>/dev/null || true
    fi
  done
  wait 2>/dev/null || true

  if (( exit_code != 0 )); then
    printf '[run-all] Services stopped with exit code %d. Logs: %s\n' "$exit_code" "$LOG_DIR" >&2
    if [[ -e /dev/tty ]]; then
      read -r -p '[run-all] Press Enter to close this Git Bash window...' _ </dev/tty || true
    fi
  fi

  return "$exit_code"
}

wait_for_eureka() {
  local attempts=30
  while (( attempts > 0 )); do
    if curl --silent --fail "${EUREKA_URL}/eureka/apps" >/dev/null 2>&1; then
      log "Eureka Server is ready at ${EUREKA_URL}"
      return
    fi
    attempts=$((attempts - 1))
    sleep 2
  done
  fail "Eureka Server did not become ready"
}

start_jar() {
  local module="$1"
  local jar="${ROOT_DIR}/${module}/target/${module}-1.0.0.jar"
  local log_file="${LOG_DIR}/${module}.log"

  [[ -f "$jar" ]] || fail "Built JAR not found: ${jar}"
  log "Starting ${module}"
  java -jar "$jar" >"$log_file" 2>&1 &
  PIDS+=("$!")
}

require_command java
require_command mvn
require_command curl

cd "$ROOT_DIR"
mkdir -p "$LOG_DIR"
rm -f "${LOG_DIR}"/*.log
trap stop_services EXIT INT TERM

log "Cleaning and building all modules"
mvn clean package -DskipTests

start_jar "eureka-server"
wait_for_eureka

for module in "${SERVICE_MODULES[@]}"; do
  start_jar "$module"
  sleep 2
done

log "All services are running"
log "API Gateway: http://localhost:8000"
log "Eureka Dashboard: ${EUREKA_URL}"
log "Logs: ${LOG_DIR}"
log "Press Ctrl+C to stop all services"

while true; do
  for index in "${!PIDS[@]}"; do
    if ! kill -0 "${PIDS[$index]}" 2>/dev/null; then
      fail "A service process exited unexpectedly; check ${LOG_DIR}"
    fi
  done
  sleep 5
done
