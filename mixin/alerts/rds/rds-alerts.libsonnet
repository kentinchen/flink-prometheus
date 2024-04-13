{
    _config+:: {
      ossSelector: error '必须提供oss产品选择器',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'oss作业未启动',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'oss',
                    selector:: $._config.ossSelector,
                  },
                ],
              },
        ],
    }
}