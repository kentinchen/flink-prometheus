{
    _config+:: {
      ossSelector: error '必须提供oss产品选择器',
      ossmetricName: 'intran_recv_size'
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'oss作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'oss',
                    metricName::  $._config.ossmetricName,
                    selector:: $._config.ossSelector,
                  },
                ],
              },
        ],
    }
}