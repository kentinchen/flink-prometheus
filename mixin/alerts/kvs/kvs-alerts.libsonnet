{
    _config+:: {
      ossSelector: error '必须提供kvs产品选择器',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'kvs作业未启动',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'kvs',
                    selector:: $._config.ossSelector,
                  },
                ],
              },
        ],
    }
}