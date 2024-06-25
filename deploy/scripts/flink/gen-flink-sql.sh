#!/usr/bin/env bash
SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)

echo "env: $ENV"
echo "shell dir: $SHELL_DIR"

FLINK_TEMPLATE_DIR=${SHELL_DIR/scripts/template}
FLINK_OUTPUT_DIR=$SHELL_DIR/out

echo "template dir: $FLINK_TEMPLATE_DIR"
echo "output dir: $FLINK_OUTPUT_DIR"

mkdir -p "$FLINK_OUTPUT_DIR"

#生成client文件
file="sql-client-init.sql"
envsubst <"$FLINK_TEMPLATE_DIR/../$file"  >"$FLINK_OUTPUT_DIR/../${file}"

#生成job文件
# shellcheck disable=SC2045
for file in $(ls "$FLINK_TEMPLATE_DIR")
do
  # shellcheck disable=SC2086
  envsubst <"$FLINK_TEMPLATE_DIR"/$file  >"$FLINK_OUTPUT_DIR"/${file/tsql/sql}
done
