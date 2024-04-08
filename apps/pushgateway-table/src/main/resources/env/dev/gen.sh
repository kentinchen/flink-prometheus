#!/usr/bin/env bash
source ./dev.env
cat ../../template/sls-prom-ecs.tsql |envsubst > out/sls-prom-ecs.sql
