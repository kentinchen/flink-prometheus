#!/usr/bin/env bash
ENV=prod
SHELL_DIR=$(cd "$(dirname $0)"|| exit; pwd)
ALERTMANAGER_TEMPLATE_DIR=${$SHELL_DIR/$ENV/template}

source SHELL_DIR/prod-alertmanager.env