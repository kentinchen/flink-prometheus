#!/usr/bin/env bash

# 更新jar时需要重启容器,所有容器都需要更新
# jar需要更新时，先替换deploy/libs中文件，如果新增需要修改
a=("jobmanager" "taskmanager1" "taskmanager2")
# shellcheck disable=SC2068
for cntr in ${a[@]};do
    docker exec "$cntr" bash /flink/usrlib/deploy-libs.sh
    docker restart "$cntr"
    docker exec "$cntr" cat /etc/hosts
done

