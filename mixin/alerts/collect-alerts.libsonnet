{
    _config+:: {
      pushgatewaySelector: 'job="pushgateway"',
      prometheusSelector: 'job="prometheus"',
      alertmangerSelector: 'job="alertmanager"',
      nodeExporterSelector: 'job="node_exporter"',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'pushgateway未上报',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'pushgateway',
                    selector:: $._config.pushgatewaySelector,
                  },
                ],
              },
              {
                name: 'prometheus未上报',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'prometheus',
                    selector:: $._config.prometheusSelector,
                  },
                ],
              },
              {
                name: 'alertmanager未上报',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'alertmanager',
                    selector:: $._config.alertmangerSelector,
                  },
                ],
              },
             {
               name: 'node-exporter未上报',
               rules: [
                 (import '../lib/utils/absent_alert.libsonnet') {
                   componentName:: 'node-exporter',
                   selector:: $._config.nodeExporterSelector,
                 },
               ],
             },
        ],
    }
}