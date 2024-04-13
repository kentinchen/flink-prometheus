{
    _config+:: {
      kubeProxySelector: 'must provide selector for kube-proxy',
    },

    prometheusAlerts+:: {
        groups+: [
              {
                name: 'kubernetes-system-kube-proxy',
                rules: [
                  (import '../lib/utils/absent_alert.libsonnet') {
                    componentName:: 'KubeProxy',
                    selector:: $._config.kubeProxySelector,
                  },
                ],
              },
        ],
    }
}