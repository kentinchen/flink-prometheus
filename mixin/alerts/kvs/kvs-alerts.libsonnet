{
    _config+:: {
      ossSelector: error '必须提供kvs产品选择器',
      kvsmetricName: 'vm_CPUUtilization'
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'kvs作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'kvs',
                    metricName::  $._config.kvsmetricName,
                    selector:: $._config.ossSelector,
                  },
                ],
              },
        ],
    }
}