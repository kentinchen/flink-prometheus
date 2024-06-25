#!/usr/bin/env bash
SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)
FLINK_TEMPLATE_DIR=${SHELL_DIR/scripts/template}

file="sls_ping_test.py"
envsubst <"$FLINK_TEMPLATE_DIR/$file"  >"$SHELL_DIR/$file"

#file="docker-compose-flink.yml"
#../env/hosts-dev
#envsubst <"$FLINK_TEMPLATE_DIR/$file"  >"$SHELL_DIR/$file"