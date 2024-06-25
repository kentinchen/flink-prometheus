#!/usr/bin/env bash
SHELL_DIR=$(cd "$(dirname "$0")"|| exit; pwd)

sh "$SHELL_DIR"/gen-flink-sql.sh

# 复制脚本
docker cp "$SHELL_DIR"/sql-client-init.sql jobmanager:/opt/flink/sql/

# 只需在jobmanager上运行部署作业
docker exec jobmanager bash /flink/usrlib/deploy-jobs.sh
