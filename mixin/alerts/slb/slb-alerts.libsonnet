{
    _config+:: {
      slbSelector: error '必须提供slb产品选择器',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'slb作业未启动',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'slb',
                    selector:: $._config.slbSelector,
                  },
                ],
              },
        ],
    }
}