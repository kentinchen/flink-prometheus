{
    _config+:: {
      rdsSelector: error '必须提供oss产品选择器',
      rdsmetricName: 'Queries_ps'
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'rds作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'rds',
                    metricName::  $._config.rdsmetricName,
                    selector:: $._config.rdsSelector,
                  },
                ],
              },
        ],
    }
}