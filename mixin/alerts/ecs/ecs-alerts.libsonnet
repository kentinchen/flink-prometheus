{
    _config+:: {
      ecsSelector: error '必须提供ecs产品选择器',
      ecsmetricName: 'vm_CPUUtilization'
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'ecs作业未启动',
                rules: [
                  (import '../lib/utils/absent_metric.libsonnet') {
                    componentName:: 'ecs',
                    metricName::  $._config.ecsmetricName,
                    selector:: $._config.ecsSelector,
                  },
                ],
              },
        ],
    }
}