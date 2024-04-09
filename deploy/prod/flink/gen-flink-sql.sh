#!/usr/bin/env bash
ENV=prod
SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)
FLINK_TEMPLATE_DIR=${$SHELL_DIR/$ENV/template}

source SHELL_DIR/prod-flink.env
envsubst <$FLINK_TEMPLATE_DIR/sls-prom-ecs.tsql  >out/sls-prom-ecs.sql
