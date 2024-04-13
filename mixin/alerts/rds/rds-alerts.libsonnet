{
    _config+:: {
      rdsSelector: error '必须提供oss产品选择器',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'rds作业未启动',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'rds',
                    selector:: $._config.rdsSelector,
                  },
                ],
              },
        ],
    }
}