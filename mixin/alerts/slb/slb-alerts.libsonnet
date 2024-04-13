{
    _config+:: {
      slbSelector: error '必须提供slb产品选择器',
      slbmetricName: 'vm_CPUUtilization'
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'slb作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'slb',
                    metricName::  $._config.slbmetricName,
                    selector:: $._config.slbSelector,
                  },
                ],
              },
        ],
    }
}