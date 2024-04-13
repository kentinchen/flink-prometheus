{
    _config+:: {
      ecsSelector: error '必须提供ecs产品选择器',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'ecs作业未启动',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'ecs',
                    selector:: $._config.ecsSelector,
                  },
                ],
              },
        ],
    }
}