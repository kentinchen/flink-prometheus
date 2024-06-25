#!/usr/bin/env bash

FLINK_SCRIPTS_DIR="/opt/flink/scripts"
# shellcheck disable=SC2045
for file in $(ls "$FLINK_SCRIPTS_DIR")
do
   /opt/flink/bin/sql-client.sh -f /opt/flink/scripts/"$file"
done

