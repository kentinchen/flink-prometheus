#!/usr/bin/env bash
source ./17e.env
cat ../../template/sls-prom-ecs.tsql |envsubst > out/sls-prom-ecs.sql
