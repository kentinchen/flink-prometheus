#!/usr/bin/env bash
SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)
source "$SHELL_DIR"/env
echo "env: $ENV"
echo "shell dir: $SHELL_DIR"

FLINK_TEMPLATE_DIR=${SHELL_DIR/$ENV/template}
FLINK_OUTPUT_DIR=$SHELL_DIR/out

echo "template dir: $FLINK_TEMPLATE_DIR"
echo "output dir: $FLINK_OUTPUT_DIR"

envsubst <"$FLINK_TEMPLATE_DIR"/gen-prom.tsql  >"$FLINK_OUTPUT_DIR"/gen-prom.sql
envsubst <"$FLINK_TEMPLATE_DIR"/sls-prom-kv.tsql  >"$FLINK_OUTPUT_DIR"/sls-prom-kv.sql
envsubst <"$FLINK_TEMPLATE_DIR"/sls-prom-ecs.tsql  >"$FLINK_OUTPUT_DIR"/sls-prom-ecs.sql
envsubst <"$FLINK_TEMPLATE_DIR"/sls-prom-oss.tsql  >"$FLINK_OUTPUT_DIR"/sls-prom-oss.sql
