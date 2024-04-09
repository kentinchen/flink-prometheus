#!/usr/bin/env bash

SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)
LIB_DIR={SHELL_DIR}/..

a=("jobmanager" "taskmanager1" "taskmanager2")
# shellcheck disable=SC2068
for cntr in ${a[@]};do
    docker exec $cntr bash /flink/usrlib/deploy.sh
done

docker cp flink/sql-client-init.sql jobmanager:/opt/flink/data/

a=("jobmanager"  "taskmanager1" "taskmanager2")
# shellcheck disable=SC2068
for cntr in ${a[@]};do
    docker restart $cntr
    docker exec $cntr cat /etc/hosts
done